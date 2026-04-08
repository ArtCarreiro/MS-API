package com.amc.api.interfaces;

import com.amc.api.dto.CustomerDTO;
import com.amc.api.entities.Customer;

public interface CustomerBO {

    Customer createCustomer(Customer data);

    Customer updateCustomer(CustomerDTO data, String uuid);

    boolean deleteCustomer(String uuid);

    void validateCustomer(Customer customer);
    
}
