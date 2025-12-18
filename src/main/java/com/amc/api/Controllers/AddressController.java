package com.amc.api.Controllers;


import com.amc.api.Entities.Address;
import com.amc.api.Repositories.AddressRepository;
import com.amc.api.Services.AddressService;
import com.amc.api.Utils.Exceptions;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private AddressRepository addressRepository;

    @GetMapping("/{customerUuid}")
    public ResponseEntity<Address> getAddressByCustomerUuid(@PathVariable(name="customerUuid") String addressUuid) {
        if (addressRepository.findByUuid(addressUuid) == null)
            throw new Exceptions.ResourceNotFoundException("Endereço não encontrado.");
        Address address = addressService.findAddressByUuid(addressUuid);
        return address != null ? ResponseEntity.ok().body(address) : ResponseEntity.badRequest().build();
    }

    @GetMapping("all/{customerUuid}")
    public ResponseEntity<Address> getAllAddressesByCustomerUuid(@PathVariable(name="customerUuid") String customerUuid) {
        Address address = addressService.findAllAddressByCustomerUuid(customerUuid);
        return address != null ? ResponseEntity.ok().body(address) : ResponseEntity.notFound().build();
    }

    @PostMapping("/{customerUuid}")
    public ResponseEntity<Address> createAddress(@Valid @RequestBody Address newAddress, @PathVariable(name="customerUuid") String customerUuid) {
       boolean address = addressService.createAddress(newAddress, customerUuid);
        return address ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @PutMapping("/{addressUuid}")
    public ResponseEntity<Address> updateAddress(@Valid @RequestBody Address updatedAddress, @PathVariable(name="addressUuid") String addressUuid) {
        if (addressRepository.findByUuid(addressUuid) == null)
            throw new Exceptions.ResourceNotFoundException("Endereço não encontrado.");
        Address address = addressService.updateAddress(updatedAddress, addressUuid);
        return address != null ? ResponseEntity.ok().body(address) : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{addressUuid}")
    public ResponseEntity<?> deleteAddress(@PathVariable(name="addressUuid") String addressUuid){
        if (addressRepository.findByUuid(addressUuid) == null)
            throw new Exceptions.ResourceNotFoundException("Endereço não encontrado.");
        boolean address = addressService.deleteAddressByUuid(addressUuid);
        return address ? ResponseEntity.noContent().build() : ResponseEntity.badRequest().build();
    }
}
