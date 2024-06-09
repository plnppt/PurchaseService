package ru.ecomshop.purchaseservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ecomshop.purchaseservice.model.entity.Purchase;

import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findByUserId(Long userId);
}
