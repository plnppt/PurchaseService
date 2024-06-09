package ru.ecomshop.purchaseservice.model.dto.response;

public record PurchaseItemResponse(
        Long id,
        Long productId,
        int quantity
) {}
