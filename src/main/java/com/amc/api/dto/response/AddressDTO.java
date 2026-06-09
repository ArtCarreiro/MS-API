package com.amc.api.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDTO {
    private String uuid;
    private String street;
    private String neighborhood;
    private String country;
    private String zipCode;
    private String complement;
}
