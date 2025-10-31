package com.amc.api.Entities.Order;


import com.amc.api.Entities.Address;
import com.amc.api.Entities.Base;
import com.amc.api.Entities.Customer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="orders")
@SQLDelete(sql = "UPDATE orders SET deleted = true WHERE uuid=? ")
public class Order extends Base {

    private String status;
    private Integer idOrder;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItems> orderItems;

    @OneToOne
    @JoinColumn(name = "payment_uuid")
    private OrderPaymentDetails orderPaymentDetails;

    @ManyToOne
    @JoinColumn(name = "shipping_uuid")
    private Address orderShippingDetails;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_uuid")
    private Customer customer;
}
