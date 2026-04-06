package com.amc.api.Entities;

import org.hibernate.annotations.SQLDelete;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "products")
@SQLDelete(sql = "UPDATE products SET deleted = true WHERE uuid=?")
public class Product extends Base {

    private String name;

    private Double value;

    private Integer quantity;

    private String skuCode;

    private String keywords;

}
