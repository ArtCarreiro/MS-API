package com.amc.api.Controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amc.api.Entities.Customer;
import com.amc.api.Interfaces.CustomerBO;
import com.amc.api.Repositories.CustomerRepository;

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
        Customer customer = null;
        if (data != null) 
            customer = customerBO.createCustomer(data);
        return customer != null ? ResponseEntity.ok(customer) : ResponseEntity.badRequest().build();
    }
}
