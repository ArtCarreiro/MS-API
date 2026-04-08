package com.amc.api.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import com.amc.api.dto.AddressDTO;
import com.amc.api.entities.Address;
import com.amc.api.entities.Customer;
import com.amc.api.interfaces.AddressBO;
import com.amc.api.repositories.AddressRepository;
import com.amc.api.utils.Exceptions;

@ExtendWith(MockitoExtension.class)
class AddressControllerTest {

    @Mock
    private AddressBO addressBO;

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressController addressController;

    @Test
    void getAddressByUuidShouldReturnAddressWhenFound() {
        Address address = buildAddress("address-1");
        when(addressRepository.findAll()).thenReturn(List.of(address));

        ResponseEntity<Address> response = addressController.getAddressByUuid("address-1");

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertSame(address, response.getBody());
    }

    @Test
    void getAddressByUuidShouldReturnNotFoundWhenMissing() {
        when(addressRepository.findAll()).thenReturn(List.of(buildAddress("address-1")));

        ResponseEntity<Address> response = addressController.getAddressByUuid("missing");

        assertEquals(HttpStatusCode.valueOf(404), response.getStatusCode());
    }

    @Test
    void createAddressShouldValidateAndReturnOk() {
        Address address = buildAddress("address-1");
        when(addressBO.createAddress(address)).thenReturn(address);

        ResponseEntity<Address> response = addressController.createAddress(address);

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertSame(address, response.getBody());
        verify(addressBO).validation(address);
    }

    @Test
    void createAddressShouldReturnBadRequestWhenBusinessLayerReturnsNull() {
        Address address = buildAddress("address-1");
        when(addressBO.createAddress(address)).thenReturn(null);

        ResponseEntity<Address> response = addressController.createAddress(address);

        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
        verify(addressBO).validation(address);
    }

    @Test
    void updateAddressShouldReturnOkWhenBusinessLayerUpdatesAddress() {
        Address address = buildAddress("address-1");
        AddressDTO dto = new AddressDTO();
        when(addressBO.updateAddress(dto, "address-1")).thenReturn(address);

        ResponseEntity<Address> response = addressController.updateAddress(dto, "address-1");

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertSame(address, response.getBody());
    }

    @Test
    void updateAddressShouldReturnBadRequestWhenBusinessLayerReturnsNull() {
        AddressDTO dto = new AddressDTO();
        when(addressBO.updateAddress(dto, "address-1")).thenReturn(null);

        ResponseEntity<Address> response = addressController.updateAddress(dto, "address-1");

        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
    }

    @Test
    void deleteAddressShouldThrowWhenAddressDoesNotExist() {
        when(addressRepository.findByUuid("missing")).thenReturn(null);

        Exceptions.ResourceNotFoundException exception = assertThrows(Exceptions.ResourceNotFoundException.class,
                () -> addressController.deleteAddress("missing"));

        assertEquals("Endereço não encontrado.", exception.getMessage());
    }

    @Test
    void deleteAddressShouldReturnNoContentWhenDeletionSucceeds() {
        Address address = buildAddress("address-1");
        when(addressRepository.findByUuid("address-1")).thenReturn(address);
        when(addressBO.deleteAddress("address-1")).thenReturn(true);

        ResponseEntity<Boolean> response = addressController.deleteAddress("address-1");

        assertEquals(HttpStatusCode.valueOf(204), response.getStatusCode());
    }

    @Test
    void deleteAddressShouldReturnBadRequestWhenDeletionFails() {
        Address address = buildAddress("address-1");
        when(addressRepository.findByUuid("address-1")).thenReturn(address);
        when(addressBO.deleteAddress("address-1")).thenReturn(false);

        ResponseEntity<Boolean> response = addressController.deleteAddress("address-1");

        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
    }

    private Address buildAddress(String uuid) {
        Customer customer = new Customer();
        customer.setUuid("customer-1");

        Address address = new Address();
        address.setUuid(uuid);
        address.setStreet("Rua A");
        address.setNeighborhood("Centro");
        address.setCountry("Brasil");
        address.setZipCode("12345678");
        address.setComplement("Casa");
        address.setCustomer(customer);
        return address;
    }
}
