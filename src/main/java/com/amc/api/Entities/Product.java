package com.amc.api.entities;

import org.hibernate.annotations.SQLDelete;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "products")
@SQLDelete(sql = "UPDATE products SET deleted = true WHERE uuid=?")
public class Product extends Base {

    @NotNull
    private String name;

    @NotNull
    private Double price;

    @NotNull
    private Integer quantity;

    @NotNull
    private String skuCode;

    private String keywords;

}
