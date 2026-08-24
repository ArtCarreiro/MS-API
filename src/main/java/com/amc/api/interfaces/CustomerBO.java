package com.amc.api.interfaces;

import com.amc.api.dto.response.CustomerDTO;
import com.amc.api.entities.Customer;

public interface CustomerBO {

    Customer createCustomer(Customer customerData);

    Customer updateCustomer(CustomerDTO newCustomerData, Customer oldCustomerData);

    void deleteCustomer(String customerUuid);

    void updateCustomerPassword(Customer customer, String newPassword);
}
