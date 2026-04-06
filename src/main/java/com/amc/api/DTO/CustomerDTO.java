package com.amc.api.DTO;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDTO {
    private String first_name;
    private String last_name;
    private Date birthDate;
    private String phone;
    private Boolean newsletter;
    private String document;
    private String gender; 
}
