package com.amc.api.Repositories.Order;

import com.amc.api.Entities.Order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, String> {

    Order findByUuid(String orderUuid);

}
