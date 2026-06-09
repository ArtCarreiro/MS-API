package com.amc.api.dto.response;

import com.amc.api.entities.Product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileDTO {
    private String name;
    private Product product;
}
