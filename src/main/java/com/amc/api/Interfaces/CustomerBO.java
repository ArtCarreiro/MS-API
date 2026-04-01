package com.amc.api.Interfaces;

import com.amc.api.Entities.Customer;

public interface CustomerBO {

    Customer createCustomer(Customer data);

    void validationCustomer(String email);
    
}
