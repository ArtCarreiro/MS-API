package com.amc.api.dto;

import com.amc.api.entities.Customer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDTO {
    private String street;
    private String neighborhood;
    private String country;
    private String zipCode;
    private String complement;
    private Customer customer;
}
