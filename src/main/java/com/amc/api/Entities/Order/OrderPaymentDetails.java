package com.amc.api.Entities.Order;

import com.amc.api.Entities.Base;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="order_payment_details")
public class OrderPaymentDetails extends Base {

    private String paymentMethod;
    private String paymentStatus;

    @OneToOne
    @JoinColumn(name = "order_uuid")
    private Order order;

}
