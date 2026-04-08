package com.amc.api.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.amc.api.dto.CustomerDTO;
import com.amc.api.entities.Customer;
import com.amc.api.entities.User;
import com.amc.api.enums.UserRoleEnum;
import com.amc.api.repositories.CustomerRepository;
import com.amc.api.repositories.UserRepository;
import com.amc.api.utils.Exceptions;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void createCustomerShouldSaveUserAndCustomerWhenEmailIsAvailable() {
        Customer customer = buildCustomer();
        when(userRepository.findByEmail("customer@email.com")).thenReturn(null);

        Customer result = customerService.createCustomer(customer);

        assertSame(customer, result);
        verify(userRepository).save(customer.getUser());
        verify(customerRepository).save(customer);
    }

    @Test
    void createCustomerShouldThrowWhenEmailAlreadyExists() {
        Customer customer = buildCustomer();
        when(userRepository.findByEmail("customer@email.com")).thenReturn(new User());

        Exceptions.DatabaseException exception = assertThrows(Exceptions.DatabaseException.class,
                () -> customerService.createCustomer(customer));

        assertEquals("Erro no banco de dados: E-mail: customer@email.com já cadastrado.", exception.getMessage());
        verify(userRepository, never()).save(customer.getUser());
        verify(customerRepository, never()).save(customer);
    }

    @Test
    void createCustomerShouldWrapRepositoryErrors() {
        Customer customer = buildCustomer();
        when(userRepository.findByEmail("customer@email.com")).thenReturn(null);
        when(userRepository.save(customer.getUser())).thenThrow(new IllegalStateException("falha ao salvar usuário"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> customerService.createCustomer(customer));

        assertTrue(exception.getCause() instanceof IllegalStateException);
    }

    @Test
    void updateCustomerShouldMapAndSaveWhenCustomerExists() {
        Customer customer = buildCustomer();
        CustomerDTO dto = new CustomerDTO();
        dto.setFirst_name("Novo");
        when(customerRepository.findByUuid("customer-1")).thenReturn(customer);

        Customer result = customerService.updateCustomer(dto, "customer-1");

        assertSame(customer, result);
        verify(mapper).map(dto, customer);
        verify(customerRepository).save(customer);
    }

    @Test
    void updateCustomerShouldWrapNotFoundError() {
        when(customerRepository.findByUuid("missing")).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> customerService.updateCustomer(new CustomerDTO(), "missing"));

        assertTrue(exception.getCause() instanceof RuntimeException);
        assertEquals("Customer não encontrado", exception.getCause().getMessage());
    }

    @Test
    void deleteCustomerShouldReturnTrueWhenRepositoryDeletes() {
        boolean deleted = customerService.deleteCustomer("customer-1");

        assertTrue(deleted);
        verify(customerRepository).deleteCustomerByUuid("customer-1");
    }

    @Test
    void deleteCustomerShouldWrapUnexpectedErrors() {
        doThrow(new IllegalStateException("falha ao deletar")).when(customerRepository).deleteCustomerByUuid("customer-1");

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> customerService.deleteCustomer("customer-1"));

        assertTrue(exception.getCause() instanceof IllegalStateException);
    }

    @Test
    void validationCustomerShouldThrowWhenEmailAlreadyExists() {
        when(userRepository.findByEmail("customer@email.com")).thenReturn(new User());

        Exceptions.DatabaseException exception = assertThrows(Exceptions.DatabaseException.class,
                () -> customerService.validationCustomer("customer@email.com"));

        assertEquals("Erro no banco de dados: E-mail: customer@email.com já cadastrado.", exception.getMessage());
    }

    @Test
    void validationCustomerShouldDoNothingWhenEmailIsAvailable() {
        when(userRepository.findByEmail("customer@email.com")).thenReturn(null);

        assertDoesNotThrow(() -> customerService.validationCustomer("customer@email.com"));
    }

    private Customer buildCustomer() {
        User user = new User();
        user.setUuid("user-1");
        user.setEmail("customer@email.com");
        user.setPassword("senha");
        user.setRole(UserRoleEnum.CUSTOMER);

        Customer customer = new Customer();
        customer.setUuid("customer-1");
        customer.setFirst_name("Maria");
        customer.setLast_name("Silva");
        customer.setBirthDate(new Date());
        customer.setPhone("11999999999");
        customer.setNewsletter(true);
        customer.setDocument("12345678901");
        customer.setGender("F");
        customer.setUser(user);
        return customer;
    }
}
