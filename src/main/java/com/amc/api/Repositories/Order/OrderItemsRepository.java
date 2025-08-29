package com.amc.api.Repositories.Order;

import com.amc.api.Entities.Order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemsRepository extends JpaRepository<Order, Long> {}
