package ru.ecomshop.purchaseservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ecomshop.purchaseservice.model.dto.request.CreatePurchaseRequest;
import ru.ecomshop.purchaseservice.model.dto.response.PurchaseResponse;
import ru.ecomshop.purchaseservice.service.PurchaseService;

import java.util.Collection;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    @GetMapping
    public ResponseEntity<Collection<PurchaseResponse>> getAllPurchases() {
        return ok(purchaseService.getAllPurchases());
    }

    @GetMapping("/{purchaseId}")
    public ResponseEntity<PurchaseResponse> getPurchaseById(@PathVariable Long purchaseId) {
        return ok(purchaseService.getPurchaseById(purchaseId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Collection<PurchaseResponse>> getPurchasesByUserId(@PathVariable Long userId) {
        return ok(purchaseService.getPurchasesByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<PurchaseResponse> createPurchase(@RequestBody CreatePurchaseRequest createPurchaseRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(purchaseService.createPurchase(createPurchaseRequest));
    }

    @PutMapping("/{purchaseId}")
    public ResponseEntity<PurchaseResponse> updatePurchaseById(@PathVariable Long purchaseId,
                                                               @RequestBody CreatePurchaseRequest updatePurchaseRequest) {
        return ok(purchaseService.updatePurchaseById(purchaseId, updatePurchaseRequest));
    }

    @DeleteMapping("/{purchaseId}")
    public ResponseEntity<Void> deletePurchaseById(@PathVariable Long purchaseId) {
        purchaseService.deletePurchaseById(purchaseId);
        return ok().build();
    }
}
