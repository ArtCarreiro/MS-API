package com.amc.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDTO {
    private String name;
    private Double price;
    private Integer estoque;
    private String keywords;
}
