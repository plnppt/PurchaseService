package ru.ecomshop.purchaseservice.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import ru.ecomshop.purchaseservice.model.dto.request.CreatePurchaseRequest;
import ru.ecomshop.purchaseservice.model.dto.response.PurchaseResponse;
import ru.ecomshop.purchaseservice.model.entity.Purchase;

@Mapper(componentModel = "spring")
public interface PurchaseMapper {

    PurchaseMapper INSTANCE = Mappers.getMapper(PurchaseMapper.class);

    @Mapping(target = "purchaseDate", expression = "java(new java.util.Date())")
    Purchase toPurchase(CreatePurchaseRequest dto);

    @Mapping(source = "items", target = "items")
    PurchaseResponse toPurchaseResponse(Purchase purchase);

    void updatePurchaseFromRequest(CreatePurchaseRequest dto, @MappingTarget Purchase purchase);
}
