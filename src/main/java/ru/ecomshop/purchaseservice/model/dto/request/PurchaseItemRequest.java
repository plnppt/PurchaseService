package ru.ecomshop.purchaseservice.model.dto.request;

public record PurchaseItemRequest(
        Long productId,
        int quantity
) {}
