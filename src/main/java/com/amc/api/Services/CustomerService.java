package com.amc.api.Services;

import java.beans.Transient;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amc.api.DTO.CustomerDTO;
import com.amc.api.Entities.Customer;
import com.amc.api.Interfaces.CustomerBO;
import com.amc.api.Repositories.CustomerRepository;
import com.amc.api.Repositories.UserRepository;
import com.amc.api.Utils.Exceptions;

import jakarta.transaction.Transactional;

@Service
public class CustomerService implements CustomerBO {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    @Transient
    public Customer createCustomer(Customer data) {
        validationCustomer(data.getUser().getEmail());
        try {
            userRepository.save(data.getUser());
            customerRepository.save(data);
            return data;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public Customer updateCustomer(CustomerDTO data, String uuid){
        try {
            Customer customer = customerRepository.findByUuid(uuid);
            if (customer == null) 
                throw new RuntimeException("Customer não encontrado");
            mapper.map(data, customer);
            customerRepository.save(customer);
            return customer;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public boolean deleteCustomer(String uuid){
        try {
            customerRepository.deleteCustomerByUuid(uuid);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void validationCustomer(String email) {
        if (userRepository.findByEmail(email) != null)
            throw new Exceptions.DatabaseException("E-mail: " + email + " já cadastrado.");
    }
}
