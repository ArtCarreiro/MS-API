package com.amc.api.Interfaces;

import com.amc.api.Entities.Customer;

public interface CustomerBO {

    Customer createCustomer(Customer customer);

    void validationCustomer(String email);
    
}
