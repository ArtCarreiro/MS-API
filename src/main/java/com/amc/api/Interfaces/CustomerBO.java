package com.amc.api.Interfaces;

import com.amc.api.DTO.CustomerDTO;
import com.amc.api.Entities.Customer;

public interface CustomerBO {

    Customer createCustomer(Customer data);

    Customer updateCustomer(CustomerDTO data, String uuid);

    boolean deleteCustomer(String uuid);

    void validationCustomer(String email);
    
}
