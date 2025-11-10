package com.amc.api.Repositories.Order;

import com.amc.api.Entities.Order.OrderPaymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderPaymentDetailsRepository extends JpaRepository<OrderPaymentDetails, Long> {}
