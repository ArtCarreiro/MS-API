package com.amc.api.Services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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

import com.amc.api.DTO.AddressDTO;
import com.amc.api.Entities.Address;
import com.amc.api.Entities.Customer;
import com.amc.api.Repositories.AddressRepository;
import com.amc.api.Repositories.CustomerRepository;
import com.amc.api.Utils.Exceptions;

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
    void createAddressShouldSaveAddressAndRefreshCustomer() {
        Address address = buildAddress();
        Customer customer = address.getCustomer();
        when(addressRepository.save(address)).thenReturn(address);
        when(customerRepository.findByUuid("customer-1")).thenReturn(customer);

        Address result = addressService.createAddress(address);

        assertSame(address, result);
        verify(addressRepository).save(address);
        verify(customerRepository).findByUuid("customer-1");
        verify(customerRepository).save(customer);
    }

    @Test
    void createAddressShouldWrapUnexpectedErrors() {
        Address address = buildAddress();
        when(addressRepository.save(address)).thenThrow(new IllegalStateException("falha ao salvar endereço"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> addressService.createAddress(address));

        assertTrue(exception.getCause() instanceof IllegalStateException);
    }

    @Test
    void updateAddressShouldMapAndSaveWhenAddressExists() {
        Address address = buildAddress();
        AddressDTO dto = new AddressDTO();
        dto.setStreet("Rua Nova");
        when(addressRepository.findByUuid("address-1")).thenReturn(address);
        when(addressRepository.save(address)).thenReturn(address);

        Address result = addressService.updateAddress(dto, "address-1");

        assertSame(address, result);
        verify(mapper).map(dto, address.getClass());
        verify(addressRepository).save(address);
    }

    @Test
    void updateAddressShouldWrapNotFoundError() {
        when(addressRepository.findByUuid("missing")).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> addressService.updateAddress(new AddressDTO(), "missing"));

        assertTrue(exception.getCause() instanceof RuntimeException);
        assertEquals("Endereço não encontrado", exception.getCause().getMessage());
    }

    @Test
    void deleteAddressShouldReturnTrueWhenRepositoryDeletes() {
        boolean deleted = addressService.deleteAddress("address-1");

        assertTrue(deleted);
        verify(addressRepository).deleteAddressByUuid("address-1");
    }

    @Test
    void deleteAddressShouldWrapUnexpectedErrors() {
        doThrow(new IllegalStateException("falha ao deletar")).when(addressRepository).deleteAddressByUuid("address-1");

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> addressService.deleteAddress("address-1"));

        assertTrue(exception.getCause() instanceof IllegalStateException);
    }

    @Test
    void validationShouldThrowWhenZipCodeIsShorterThanExpected() {
        Address address = buildAddress();
        address.setZipCode("1234567");

        Exceptions.InvalidRequestException exception = assertThrows(Exceptions.InvalidRequestException.class,
                () -> addressService.validation(address));

        assertEquals("Requisição inválida: CEP não esta no formato correto.", exception.getMessage());
    }

    @Test
    void validationShouldDoNothingWhenZipCodeHasValidLength() {
        Address address = buildAddress();

        assertDoesNotThrow(() -> addressService.validation(address));
    }

    private Address buildAddress() {
        Customer customer = new Customer();
        customer.setUuid("customer-1");

        Address address = new Address();
        address.setUuid("address-1");
        address.setStreet("Rua A");
        address.setNeighborhood("Centro");
        address.setCountry("Brasil");
        address.setZipCode("12345678");
        address.setComplement("Apto 10");
        address.setCustomer(customer);
        return address;
    }
}
