package com.amc.api.services;

import java.beans.Transient;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amc.api.dto.CustomerDTO;
import com.amc.api.entities.Customer;
import com.amc.api.interfaces.CustomerBO;
import com.amc.api.repositories.CustomerRepository;
import com.amc.api.repositories.UserRepository;
import com.amc.api.utils.Exceptions;

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
        validateCustomer(data);
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
        Customer customer = customerRepository.findByUuid(uuid);
        validateCustomer(customer);
        try {
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
    public void validateCustomer(Customer customer) {
        if (customerRepository.findByUuid(customer.getUuid()) == null) 
            throw new Exceptions.ResourceNotFoundException("Cliente não encontrado");
        if (customerRepository.findByPhone(customer.getPhone()) != null)
            throw new Exceptions.DatabaseException("Telefone: " + customer.getPhone() + " já cadastrado.");
    }
}
