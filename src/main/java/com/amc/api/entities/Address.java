package com.amc.api.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "addresses")
public class Address extends Base {
    
    @NotNull
    @Column(nullable = false)
    private String street;

    @NotNull
    @Column(nullable = false)
    private String neighborhood;

    @NotNull
    @Column(nullable = false)
    private String country;

    @NotNull
    @Column(nullable = false)
    private String zipCode;

    @NotNull
    @Column(length = 500, nullable = false)
    private String complement;

    @ManyToOne
    @JoinColumn(name = "customer_uuid")
    private Customer customer;
    
}