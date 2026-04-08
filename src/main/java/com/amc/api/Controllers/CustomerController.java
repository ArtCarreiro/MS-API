package com.amc.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amc.api.dto.CustomerDTO;
import com.amc.api.entities.Customer;
import com.amc.api.interfaces.CustomerBO;
import com.amc.api.repositories.CustomerRepository;
import com.amc.api.utils.Exceptions;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerBO customerBO;

    @GetMapping
    public ResponseEntity<List<Customer>> getActiveCustomers() {
        List<Customer> customers = customerRepository.findAll()
                .stream()
                .filter(Customer::getActive)
                .toList();
        return customers.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(customers);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Customer> getCustomerByUuid(@PathVariable String uuid) {
        return customerRepository.findAll().stream()
                .filter(customer -> uuid.equals(customer.getUuid()))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer data) {
        Customer customer = customerBO.createCustomer(data);
        return customer != null ? ResponseEntity.ok(customer) : ResponseEntity.badRequest().build();
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Customer> updateCustomer(@Valid @RequestBody CustomerDTO data, @PathVariable String uuid) {
        Customer customer = customerBO.updateCustomer(data, uuid);
        return customer != null ? ResponseEntity.ok(customer) : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Boolean> deleteCustomer(@PathVariable String uuid) {
        Customer customer = customerRepository.findByUuid(uuid);
        if (customer == null)
            throw new Exceptions.ResourceNotFoundException("Cliente não encontrado.");
        return customerBO.deleteCustomer(uuid) == true ? ResponseEntity.noContent().build() : ResponseEntity.badRequest().build();
    }

}
