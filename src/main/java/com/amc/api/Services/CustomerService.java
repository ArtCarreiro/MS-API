package com.amc.api.Services;

import com.amc.api.Entities.Customer;
import com.amc.api.Repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;


    public Customer getCustomerByUuid(String uuid) {
        return customerRepository.findByUuid(uuid);
    }

}
