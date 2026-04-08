package com.amc.api.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    void createCustomerShouldPersistUserAndCustomer() {
        Customer customer = buildCustomer();

        when(customerRepository.findByUuid(customer.getUuid())).thenReturn(customer);
        when(customerRepository.findByPhone(customer.getPhone())).thenReturn(null);
        when(userRepository.save(customer.getUser())).thenReturn(customer.getUser());
        when(customerRepository.save(customer)).thenReturn(customer);

        Customer result = customerService.createCustomer(customer);

        assertSame(customer, result);
        verify(userRepository).save(customer.getUser());
        verify(customerRepository).save(customer);
    }

    @Test
    void createCustomerShouldThrowWhenCustomerIsNotFoundInValidation() {
        Customer customer = buildCustomer();

        when(customerRepository.findByUuid(customer.getUuid())).thenReturn(null);

        Exceptions.ResourceNotFoundException exception = assertThrows(
                Exceptions.ResourceNotFoundException.class,
                () -> customerService.createCustomer(customer));

        assertEquals("Cliente não encontrado", exception.getMessage());
    }

    @Test
    void createCustomerShouldThrowWhenPhoneAlreadyExists() {
        Customer customer = buildCustomer();

        when(customerRepository.findByUuid(customer.getUuid())).thenReturn(customer);
        when(customerRepository.findByPhone(customer.getPhone())).thenReturn(new Customer());

        Exceptions.DatabaseException exception = assertThrows(
                Exceptions.DatabaseException.class,
                () -> customerService.createCustomer(customer));

        assertEquals("Erro no banco de dados: Telefone: 11999999999 já cadastrado.", exception.getMessage());
    }

    @Test
    void updateCustomerShouldMapAndSaveWhenValidationPasses() {
        Customer customer = buildCustomer();
        CustomerDTO dto = new CustomerDTO();
        dto.setPhone("11888888888");

        when(customerRepository.findByUuid(customer.getUuid())).thenReturn(customer);
        when(customerRepository.findByPhone(customer.getPhone())).thenReturn(null);
        when(customerRepository.save(customer)).thenReturn(customer);

        Customer result = customerService.updateCustomer(dto, customer.getUuid());

        assertSame(customer, result);
        verify(mapper).map(dto, customer);
        verify(customerRepository).save(customer);
    }

    @Test
    void updateCustomerShouldWrapUnexpectedErrors() {
        Customer customer = buildCustomer();
        CustomerDTO dto = new CustomerDTO();
        RuntimeException failure = new RuntimeException("mapper-failed");

        when(customerRepository.findByUuid(customer.getUuid())).thenReturn(customer);
        when(customerRepository.findByPhone(customer.getPhone())).thenReturn(null);
        doThrow(failure).when(mapper).map(dto, customer);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> customerService.updateCustomer(dto, customer.getUuid()));

        assertSame(failure, exception.getCause());
    }

    @Test
    void deleteCustomerShouldReturnTrueWhenRepositoryDeletes() {
        boolean result = customerService.deleteCustomer("customer-uuid");

        assertTrue(result);
        verify(customerRepository).deleteCustomerByUuid("customer-uuid");
    }

    @Test
    void deleteCustomerShouldWrapRepositoryErrors() {
        RuntimeException failure = new RuntimeException("delete-failed");
        doThrow(failure).when(customerRepository).deleteCustomerByUuid("customer-uuid");

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> customerService.deleteCustomer("customer-uuid"));

        assertSame(failure, exception.getCause());
    }

    @Test
    void validateCustomerShouldThrowWhenUuidIsNotFound() {
        Customer customer = buildCustomer();

        when(customerRepository.findByUuid(customer.getUuid())).thenReturn(null);

        assertThrows(Exceptions.ResourceNotFoundException.class, () -> customerService.validateCustomer(customer));
    }

    @Test
    void validateCustomerShouldThrowWhenPhoneAlreadyExists() {
        Customer customer = buildCustomer();

        when(customerRepository.findByUuid(customer.getUuid())).thenReturn(customer);
        when(customerRepository.findByPhone(customer.getPhone())).thenReturn(new Customer());

        assertThrows(Exceptions.DatabaseException.class, () -> customerService.validateCustomer(customer));
    }

    private Customer buildCustomer() {
        User user = new User();
        user.setUuid("user-uuid");
        user.setEmail("customer@test.com");
        user.setPassword("123456");
        user.setRole(UserRoleEnum.ADMINISTRATOR);

        Customer customer = new Customer();
        customer.setUuid("customer-uuid");
        customer.setFirst_name("Maria");
        customer.setLast_name("Silva");
        customer.setPhone("11999999999");
        customer.setDocument("12345678900");
        customer.setUser(user);
        return customer;
    }
}
