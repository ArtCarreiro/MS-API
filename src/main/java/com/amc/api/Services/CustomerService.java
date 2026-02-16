package com.amc.api.Services;

import com.amc.api.Entities.Customer;
import com.amc.api.Repositories.CustomerRepository;
import com.amc.api.Repositories.UserRepository;
import com.amc.api.Utils.Exceptions;

import java.beans.Transient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    @Transient
    public Customer createCustomer(Customer customer) {
        validationCustomer(customer.getUser().getEmail());
        try {
            userRepository.save(customer.getUser());
            customerRepository.save(customer);
            return customer;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void validationCustomer(String email) {
        if (userRepository.findByEmail(email) != null)
            throw new Exceptions.DatabaseException("E-mail: " + email + " já cadastrado.");
    }
}
