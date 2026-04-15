package com.amc.api.entities;

import org.hibernate.annotations.SQLDelete;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "products")
@SQLDelete(sql = "UPDATE products SET deleted = true WHERE uuid=?")
public class Product extends Base {

    @NotNull
    private String name;

    @NotNull
    private Double price;

    @NotNull
    private Integer estoque;

    @NotNull
    @Column(updatable = false)
    private String skuCode;

    private String keywords;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<File> files;

}
