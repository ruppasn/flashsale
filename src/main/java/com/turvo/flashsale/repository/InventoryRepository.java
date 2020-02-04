package com.turvo.flashsale.repository;

import com.turvo.flashsale.model.Inventory;
import com.turvo.flashsale.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
}