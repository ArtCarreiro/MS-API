package com.amc.api.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import com.amc.api.dto.CustomerDTO;
import com.amc.api.entities.Customer;
import com.amc.api.entities.User;
import com.amc.api.enums.UserRoleEnum;
import com.amc.api.interfaces.CustomerBO;
import com.amc.api.repositories.CustomerRepository;
import com.amc.api.utils.Exceptions;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerBO customerBO;

    @InjectMocks
    private CustomerController customerController;

    @Test
    void getActiveCustomersShouldReturnOnlyActiveCustomers() {
        Customer activeCustomer = buildCustomer("customer-1", true);
        Customer inactiveCustomer = buildCustomer("customer-2", false);
        when(customerRepository.findAll()).thenReturn(List.of(activeCustomer, inactiveCustomer));

        ResponseEntity<List<Customer>> response = customerController.getActiveCustomers();
        List<Customer> body = response.getBody();

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertNotNull(body);
        assertEquals(1, body.size());
        assertSame(activeCustomer, body.getFirst());
    }

    @Test
    void getActiveCustomersShouldReturnNoContentWhenThereAreNoActiveCustomers() {
        when(customerRepository.findAll()).thenReturn(List.of(buildCustomer("customer-1", false)));

        ResponseEntity<List<Customer>> response = customerController.getActiveCustomers();

        assertEquals(HttpStatusCode.valueOf(204), response.getStatusCode());
    }

    @Test
    void getCustomerByUuidShouldReturnCustomerWhenFound() {
        Customer customer = buildCustomer("customer-1", true);
        when(customerRepository.findAll()).thenReturn(List.of(customer));

        ResponseEntity<Customer> response = customerController.getCustomerByUuid("customer-1");

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertSame(customer, response.getBody());
    }

    @Test
    void getCustomerByUuidShouldReturnNotFoundWhenMissing() {
        when(customerRepository.findAll()).thenReturn(List.of(buildCustomer("customer-1", true)));

        ResponseEntity<Customer> response = customerController.getCustomerByUuid("missing");

        assertEquals(HttpStatusCode.valueOf(404), response.getStatusCode());
    }

    @Test
    void createCustomerShouldReturnOkWhenBusinessLayerCreatesCustomer() {
        Customer customer = buildCustomer("customer-1", true);
        when(customerBO.createCustomer(customer)).thenReturn(customer);

        ResponseEntity<Customer> response = customerController.createCustomer(customer);

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertSame(customer, response.getBody());
    }

    @Test
    void createCustomerShouldReturnBadRequestWhenBodyIsNull() {
        ResponseEntity<Customer> response = customerController.createCustomer(null);

        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
    }

    @Test
    void createCustomerShouldReturnBadRequestWhenBusinessLayerReturnsNull() {
        Customer customer = buildCustomer("customer-1", true);
        when(customerBO.createCustomer(customer)).thenReturn(null);

        ResponseEntity<Customer> response = customerController.createCustomer(customer);

        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
    }

    @Test
    void updateCustomerShouldReturnOkWhenBusinessLayerUpdatesCustomer() {
        Customer customer = buildCustomer("customer-1", true);
        CustomerDTO dto = new CustomerDTO();
        when(customerBO.updateCustomer(dto, "customer-1")).thenReturn(customer);

        ResponseEntity<Customer> response = customerController.updateCustomer(dto, "customer-1");

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertSame(customer, response.getBody());
    }

    @Test
    void updateCustomerShouldReturnBadRequestWhenBusinessLayerReturnsNull() {
        CustomerDTO dto = new CustomerDTO();
        when(customerBO.updateCustomer(dto, "customer-1")).thenReturn(null);

        ResponseEntity<Customer> response = customerController.updateCustomer(dto, "customer-1");

        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
    }

    @Test
    void deleteCustomerShouldThrowWhenCustomerDoesNotExist() {
        when(customerRepository.findByUuid("missing")).thenReturn(null);

        Exceptions.ResourceNotFoundException exception = assertThrows(Exceptions.ResourceNotFoundException.class,
                () -> customerController.deleteCustomer("missing"));

        assertEquals("Cliente não encontrado.", exception.getMessage());
    }

    @Test
    void deleteCustomerShouldReturnNoContentWhenDeletionSucceeds() {
        Customer customer = buildCustomer("customer-1", true);
        when(customerRepository.findByUuid("customer-1")).thenReturn(customer);
        when(customerBO.deleteCustomer("customer-1")).thenReturn(true);

        ResponseEntity<Boolean> response = customerController.deleteCustomer("customer-1");

        assertEquals(HttpStatusCode.valueOf(204), response.getStatusCode());
    }

    @Test
    void deleteCustomerShouldReturnBadRequestWhenDeletionFails() {
        Customer customer = buildCustomer("customer-1", true);
        when(customerRepository.findByUuid("customer-1")).thenReturn(customer);
        when(customerBO.deleteCustomer("customer-1")).thenReturn(false);

        ResponseEntity<Boolean> response = customerController.deleteCustomer("customer-1");

        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
    }

    private Customer buildCustomer(String uuid, boolean active) {
        User user = new User();
        user.setUuid("user-" + uuid);
        user.setEmail(uuid + "@email.com");
        user.setPassword("senha");
        user.setRole(UserRoleEnum.CUSTOMER);

        Customer customer = new Customer();
        customer.setUuid(uuid);
        customer.setFirst_name("Maria");
        customer.setLast_name("Silva");
        customer.setPhone("11999999999");
        customer.setDocument("12345678901");
        customer.setActive(active);
        customer.setDeleted(false);
        customer.setUser(user);
        return customer;
    }
}
