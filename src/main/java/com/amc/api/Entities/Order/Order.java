package com.amc.api.Entities.Order;


import com.amc.api.Entities.Base;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="orders")
@SQLDelete(sql = "UPDATE orders SET deleted = true WHERE uuid=? ")
public class Order extends Base {

    private String status;
    private Integer idOrder;











    // private OrderItems orderItems;
    // private OrderShippingDetails orderShippingDetails;
    // private OrderPaymentDetails orderPaymentDetails;

}
