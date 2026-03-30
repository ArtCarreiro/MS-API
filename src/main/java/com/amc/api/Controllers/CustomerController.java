package com.amc.api.Controllers;


import com.amc.api.Entities.Customer;
import com.amc.api.Repositories.CustomerRepository;
import com.amc.api.Interfaces.CustomerBO;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer customer) {
        Customer newCustomer = null;
        if (customer != null) 
            newCustomer = customerBO.createCustomer(customer);
        return newCustomer != null ? ResponseEntity.ok(newCustomer) : ResponseEntity.badRequest().build();
    }
}
