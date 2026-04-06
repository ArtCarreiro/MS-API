package com.amc.api.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amc.api.DTO.AddressDTO;
import com.amc.api.Entities.Address;
import com.amc.api.Interfaces.AddressBO;
import com.amc.api.Repositories.AddressRepository;
import com.amc.api.Utils.Exceptions;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/address")
public class AddressController {
    
    @Autowired
    private AddressBO addressBO;

    @Autowired
    private AddressRepository addressRepository;

    
    @GetMapping("/{uuid}")
    public ResponseEntity<Address> getAddressByUuid(@PathVariable String uuid) {
        return addressRepository.findAll().stream()
            .filter(address -> uuid.equals(address.getUuid()))
            .findFirst()
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Address> createAddress(@Valid @RequestBody Address data) {
        addressBO.validation(data);
        Address address = addressBO.createAddress(data);
        return address != null ? ResponseEntity.ok(address) : ResponseEntity.badRequest().build();
    }
    
    @PutMapping("/{uuid}")
    public ResponseEntity<Address> updateAddress(@Valid @RequestBody AddressDTO data, @PathVariable String uuid) {
        Address address = addressBO.updateAddress(data, uuid);
        return address != null ? ResponseEntity.ok(address) : ResponseEntity.badRequest().build();
    }
     
    @DeleteMapping("/{uuid}")
    public ResponseEntity<Boolean> deleteAddress(@PathVariable String uuid) {
        Address address = addressRepository.findByUuid(uuid);
        if (address == null) 
            throw new Exceptions.ResourceNotFoundException("Endereço não encontrado.");
        return addressBO.deleteAddress(uuid) == true ? ResponseEntity.noContent().build() : ResponseEntity.badRequest().build();
    }

}
