package com.amc.api.Entities;

import com.amc.api.Entities.Cart.Cart;
import com.amc.api.Entities.Order.Order;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "customers")
public class Customer extends Base {

    @NotBlank(message = "Nome é obrigatório")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank(message = "Sobrenome é obrigatório")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotBlank(message = "Documento é obrigatório")
    @Column(name = "document", nullable = false)
    private String document;

    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(name = "birthdate", nullable = false)
    private Date birthdate;

    @NotNull
    @Column(name = "phone", nullable = false)
    private String phone;

    @NotNull
    @Column(name = "gender", nullable = false)
    private String gender;

    private String profession;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_uuid", nullable = false)
    private User user;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Address> addresses;

    @OneToMany(mappedBy = "customer")
    private List<Cart> carts;

    @OneToMany(mappedBy = "customer")
    private List<Order> orders;

}
