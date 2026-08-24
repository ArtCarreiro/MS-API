package com.amc.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amc.api.dto.response.CustomerDTO;
import com.amc.api.entities.Customer;
import com.amc.api.interfaces.CustomerBO;
import com.amc.api.repositories.CustomerRepository;
import com.amc.api.utils.Exceptions;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerBO customerBO;

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer customerData) {
        Customer customer = customerBO.createCustomer(customerData);
        return customer != null ? ResponseEntity.ok(customer) : ResponseEntity.badRequest().build();
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Customer> updateCustomer(@Valid @RequestBody CustomerDTO newCustomerData, @PathVariable String customerUuid) {
        Customer customer = customerRepository.findByUuid(customerUuid).orElseThrow(() 
            -> new Exceptions.ResourceNotFoundException("Cliente não encontrado"));
        return customer != null ? ResponseEntity.ok(customerBO.updateCustomer(newCustomerData, customer)) : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable String uuid) {
        Customer customer = customerRepository.findByUuid(uuid).orElseThrow(() 
            -> new Exceptions.ResourceNotFoundException("Cliente não encontrado"));
        customerBO.deleteCustomer(customer.getUuid());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{uuid}/updatePassword")
    public ResponseEntity<Void> updateCustomerPassword(@PathVariable String uuid, @RequestBody String newPassword) {
        Customer customer = customerRepository.findByUuid(uuid).orElseThrow(() 
            -> new Exceptions.ResourceNotFoundException("Cliente não encontrado"));
        customerBO.updateCustomerPassword(customer, newPassword);
        return ResponseEntity.ok().build();
    }

}
