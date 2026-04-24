package com.amc.api.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.amc.api.dto.AddressDTO;
import com.amc.api.entities.Address;
import com.amc.api.entities.Customer;
import com.amc.api.repositories.AddressRepository;
import com.amc.api.repositories.CustomerRepository;
import com.amc.api.utils.Exceptions;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private AddressService addressService;

    @Test
    void createAddressShouldSaveAddressAndCustomer() {
        Address address = buildAddress();
        Customer customer = address.getCustomer();

        when(addressRepository.findByUuid(address.getUuid())).thenReturn(address);
        when(addressRepository.save(address)).thenReturn(address);
        when(customerRepository.findByUuid(customer.getUuid())).thenReturn(customer);
        when(customerRepository.save(customer)).thenReturn(customer);

        Address result = addressService.createAddress(address);

        assertSame(address, result);
        verify(addressRepository).save(address);
        verify(customerRepository).save(customer);
    }

    @Test
    void createAddressShouldThrowWhenAddressIsNotFoundInValidation() {
        Address address = buildAddress();

        when(addressRepository.findByUuid(address.getUuid())).thenReturn(null);

        Exceptions.ResourceNotFoundException exception = assertThrows(
                Exceptions.ResourceNotFoundException.class,
                () -> addressService.createAddress(address));

        assertEquals("Endereço não encontrado", exception.getMessage());
    }

    @Test
    void createAddressShouldThrowWhenZipCodeIsInvalid() {
        Address address = buildAddress();
        address.setZipCode("1234567");

        when(addressRepository.findByUuid(address.getUuid())).thenReturn(address);

        Exceptions.InvalidRequestException exception = assertThrows(
                Exceptions.InvalidRequestException.class,
                () -> addressService.createAddress(address));

        assertEquals("Requisição inválida: CEP não esta no formato correto.", exception.getMessage());
    }

    @Test
    void updateAddressShouldMapAndSaveWhenValidationPasses() {
        Address address = buildAddress();
        AddressDTO dto = new AddressDTO();
        dto.setStreet("Rua Nova");

        when(addressRepository.findByUuid(address.getUuid())).thenReturn(address);
        when(addressRepository.save(address)).thenReturn(address);

        Address result = addressService.updateAddress(dto, address.getUuid());

        assertSame(address, result);
        verify(mapper).map(dto, address.getClass());
        verify(addressRepository).save(address);
    }

    @Test
    void updateAddressShouldWrapUnexpectedErrors() {
        Address address = buildAddress();
        AddressDTO dto = new AddressDTO();
        RuntimeException failure = new RuntimeException("mapper-failed");

        when(addressRepository.findByUuid(address.getUuid())).thenReturn(address);
        doThrow(failure).when(mapper).map(same(dto), eq(address.getClass()));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> addressService.updateAddress(dto, address.getUuid()));

        assertSame(failure, exception.getCause());
    }

    @Test
    void deleteAddressShouldReturnTrueWhenRepositoryDeletes() {
        boolean result = addressService.deleteAddress("address-uuid");

        assertTrue(result);
        verify(addressRepository).deleteAddressByUuid("address-uuid");
    }

    @Test
    void deleteAddressShouldWrapRepositoryErrors() {
        RuntimeException failure = new RuntimeException("delete-failed");
        doThrow(failure).when(addressRepository).deleteAddressByUuid("address-uuid");

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> addressService.deleteAddress("address-uuid"));

        assertSame(failure, exception.getCause());
    }

    @Test
    void validateAddressShouldThrowWhenUuidIsNotFound() {
        Address address = buildAddress();

        when(addressRepository.findByUuid(address.getUuid())).thenReturn(null);

        assertThrows(Exceptions.ResourceNotFoundException.class, () -> addressService.validateAddress(address));
    }

    @Test
    void validateAddressShouldThrowWhenZipCodeHasLessThanEightCharacters() {
        Address address = buildAddress();
        address.setZipCode("123");

        when(addressRepository.findByUuid(address.getUuid())).thenReturn(address);

        assertThrows(Exceptions.InvalidRequestException.class, () -> addressService.validateAddress(address));
    }

    private Address buildAddress() {
        Customer customer = new Customer();
        customer.setUuid("customer-uuid");

        Address address = new Address();
        address.setUuid("address-uuid");
        address.setStreet("Rua A");
        address.setNeighborhood("Centro");
        address.setCountry("Brasil");
        address.setZipCode("12345678");
        address.setComplement("Casa");
        address.setCustomer(customer);
        return address;
    }
}
