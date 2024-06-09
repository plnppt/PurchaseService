package ru.ecomshop.purchaseservice.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import ru.ecomshop.purchaseservice.model.dto.request.CreatePurchaseRequest;
import ru.ecomshop.purchaseservice.model.dto.response.PurchaseResponse;
import ru.ecomshop.purchaseservice.model.entity.Purchase;
import ru.ecomshop.purchaseservice.model.entity.PurchaseItem;
import ru.ecomshop.purchaseservice.repository.PurchaseRepository;
import ru.ecomshop.purchaseservice.model.mapper.PurchaseMapper;

import java.net.URI;
import java.util.Collection;
import java.util.Date;

@Service
@AllArgsConstructor
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final PurchaseMapper purchaseMapper;
    private final RestTemplate restTemplate;
    private static final String PRODUCT_SERVICE_URL = "http://localhost:3031/products";

    public Collection<PurchaseResponse> getAllPurchases() {
        return purchaseRepository.findAll().stream()
                .map(purchaseMapper::toPurchaseResponse)
                .toList();
    }

    public PurchaseResponse getPurchaseById(Long id) {
        var purchase = processFindPurchaseById(id);
        return purchaseMapper.toPurchaseResponse(purchase);
    }

    public Collection<PurchaseResponse> getPurchasesByUserId(Long userId) {
        return purchaseRepository.findByUserId(userId).stream()
                .map(purchaseMapper::toPurchaseResponse)
                .toList();
    }

    public PurchaseResponse createPurchase(CreatePurchaseRequest createPurchaseRequest) {
        Purchase purchase = purchaseMapper.toPurchase(createPurchaseRequest);
        purchase.setPurchaseDate(new Date());

        purchase.getItems().forEach(item -> {
            item.setPurchase(purchase);

            // Вызов сервиса товаров для уменьшения количества товара на складе
            URI uri = URI.create(PRODUCT_SERVICE_URL + "/decrease-quantity?productId=" + item.getProductId() + "&quantity=" + item.getQuantity());
            restTemplate.postForEntity(uri, null, Void.class);
        });

        return purchaseMapper.toPurchaseResponse(purchaseRepository.save(purchase));
    }


    @Transactional
    public PurchaseResponse updatePurchaseById(Long purchaseId, CreatePurchaseRequest updatePurchaseRequest) {
        var purchase = processFindPurchaseById(purchaseId);
        purchaseMapper.updatePurchaseFromRequest(updatePurchaseRequest, purchase);

        purchase.getItems().clear();
        updatePurchaseRequest.items().forEach(itemRequest -> {
            var item = new PurchaseItem();
            item.setProductId(itemRequest.productId());
            item.setQuantity(itemRequest.quantity());
            item.setPurchase(purchase);

            // Вызов сервиса товаров для уменьшения количества товара на складе
            URI uri = URI.create(PRODUCT_SERVICE_URL + "/decrease-quantity?productId=" + item.getProductId() + "&quantity=" + item.getQuantity());
            restTemplate.postForEntity(uri, null, Void.class);

            purchase.getItems().add(item);
        });
        return purchaseMapper.toPurchaseResponse(purchase);
    }

    public void deletePurchaseById(Long purchaseId) {
        var purchase = processFindPurchaseById(purchaseId);
        purchaseRepository.delete(purchase);
    }

    private Purchase processFindPurchaseById(Long id) {
        return purchaseRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Покупки с данным идентификатором не существует")
        );
    }
}
