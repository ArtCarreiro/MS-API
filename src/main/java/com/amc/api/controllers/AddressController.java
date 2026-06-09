package com.amc.api.controllers;

import java.net.URI;

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

import com.amc.api.dto.request.AddressRequestBodyDTO;
import com.amc.api.dto.response.AddressDTO;
import com.amc.api.interfaces.AddressBO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private final AddressBO addressBO;
    
    @GetMapping("/{customerUuid}")
    public ResponseEntity<AddressDTO> getAddressByCustomerUuid(@PathVariable String customerUuid) {
        AddressDTO address = addressBO.getAddress(customerUuid);
        return ResponseEntity.ok(address);
    }

    @PostMapping("/{customerUuid}")
    public ResponseEntity<AddressDTO> createAddress(@Valid @RequestBody AddressRequestBodyDTO addressData, @PathVariable String customerUuid) {
        AddressDTO address = addressBO.createAddress(addressData, customerUuid);
        return ResponseEntity.created(URI.create("/address/" + address.getUuid())).body(address);
    }
    
    @PutMapping("/{customerUuid}")
    public ResponseEntity<AddressDTO> updateAddress(@Valid @RequestBody AddressRequestBodyDTO addressData, @PathVariable String customerUuid) {
        AddressDTO address = addressBO.updateAddress(addressData, customerUuid);
        return ResponseEntity.ok(address);
    }
     
    @DeleteMapping("/{customerUuid}")
    public ResponseEntity<Void> deleteAddress(@PathVariable String customerUuid) {
        addressBO.deleteAddress(customerUuid);
        return ResponseEntity.noContent().build();
    }
}
    