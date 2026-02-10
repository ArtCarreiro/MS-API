package com.amc.api.Controllers;


import com.amc.api.Entities.Customer;
import com.amc.api.Repositories.CustomerRepository;
import com.amc.api.Services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerService customerService;

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



}
