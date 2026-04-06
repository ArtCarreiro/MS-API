package com.amc.api.DTO;

import com.amc.api.Entities.Customer;

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
