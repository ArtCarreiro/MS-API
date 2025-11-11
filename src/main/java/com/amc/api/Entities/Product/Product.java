package com.amc.api.Entities.Product;

import com.amc.api.Entities.Base;
import com.amc.api.Entities.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="products")
@SQLDelete(sql = "UPDATE products SET deleted = true WHERE uuid=?")
public class Product extends Base {

    private long quantity;
    private String name;
    private String description;
    private double price;
    private String slug;
    private double priceWithDiscount;
    private String skuCode;
    private double width;
    private double height;
    private double depth;


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_uuid", referencedColumnName = "uuid")
    @NotNull
    private List<FileProduct> fileProducts;

    @ManyToOne
    @JoinColumn(name = "category_uuid", referencedColumnName = "uuid")
    @NotNull
    private Category category;

}
