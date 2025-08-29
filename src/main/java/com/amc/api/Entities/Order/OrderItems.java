package com.amc.api.Entities.Order;

import com.amc.api.Entities.Base;
import com.amc.api.Entities.Product.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "order_items")
public class OrderItems extends Base {


    private Integer quantity;
    private double amount;
    private String observation;
    private double unitPrice;


    @ManyToOne
    @JoinColumn(name = "order_uuid")
    private Order order;


    @ManyToOne
    @JoinColumn(name = "product_uuid")
    private Product product;

}
