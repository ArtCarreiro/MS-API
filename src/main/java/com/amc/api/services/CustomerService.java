package com.amc.api.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amc.api.dto.response.CustomerDTO;
import com.amc.api.entities.Customer;
import com.amc.api.interfaces.CustomerBO;
import com.amc.api.repositories.CustomerRepository;

import jakarta.transaction.Transactional;

@Service
public class CustomerService implements CustomerBO {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    @Transactional
    public Customer createCustomer(Customer customerData) {
        try {  
            return customerRepository.saveAndFlush(customerData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public Customer updateCustomer(CustomerDTO newCustomerData, Customer oldCustomerData) {
        try {
            mapper.map(newCustomerData, oldCustomerData);
            customerRepository.save(oldCustomerData);
            return oldCustomerData;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public void deleteCustomer(String customerUuid){
        try {
            customerRepository.deleteCustomerByUuid(customerUuid);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public void updateCustomerPassword(Customer customer, String newPassword) {
        try {
            customer.setPassword(customer.encryptPassword(newPassword));
            customerRepository.save(customer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
