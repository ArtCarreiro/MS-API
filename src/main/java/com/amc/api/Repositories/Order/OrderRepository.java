package com.amc.api.Repositories.Order;

import com.amc.api.Entities.Order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Order findByUuid(String orderUuid);

}
