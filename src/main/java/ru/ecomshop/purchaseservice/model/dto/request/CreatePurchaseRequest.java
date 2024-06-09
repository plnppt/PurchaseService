package ru.ecomshop.purchaseservice.model.dto.request;

import java.util.List;

public record CreatePurchaseRequest(
        Long userId,
        List<PurchaseItemRequest> items,
        double totalAmount
) {}

