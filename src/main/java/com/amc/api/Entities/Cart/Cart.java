package com.amc.api.Entities.Cart;

import com.amc.api.Entities.Base;
import com.amc.api.Entities.Customer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "carts")
@Getter
@Setter
public class Cart extends Base {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_uuid")
    private Customer customer;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItems> items;

    @Column(nullable = false)
    private BigDecimal totalAmount = BigDecimal.ZERO;


    // Métodos utilitários
    public void addItem(CartItems item) {
        items.add(item);
        item.setCart(this);
        recalculateTotal();
    }

    public void removeItem(CartItems item) {
        items.remove(item);
        item.setCart(null);
        recalculateTotal();
    }

    public void recalculateTotal() {
        this.totalAmount = items.stream()
                .map(CartItems::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
