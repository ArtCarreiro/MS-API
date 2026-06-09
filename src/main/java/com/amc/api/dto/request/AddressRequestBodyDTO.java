package com.amc.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressRequestBodyDTO {
    
    @NotBlank(message = "Street is required")
    private String street;

    @NotBlank(message = "Neighborhood is required")
    private String neighborhood;

    @NotBlank(message = "Country is required")
    private String country;

    @NotBlank(message = "Zip code is required")
    private String zipCode;

    private String complement;

}
