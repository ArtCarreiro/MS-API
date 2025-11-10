package com.amc.api.Controllers;


import com.amc.api.Entities.Address;
import com.amc.api.Services.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping("/{customerUuid}")
    public ResponseEntity<Address> getAddressByUuid(@PathVariable(name="customerUuid") String addressUuid) {
        Address address = addressService.findAddressByUuid(addressUuid);
        return address != null ? ResponseEntity.ok().body(address) : ResponseEntity.badRequest().build();
    }

    @GetMapping("all/{customerUuid}")
    public ResponseEntity<Address> getAllAddressesBycustomerUuid(@PathVariable(name="customerUuid") String customerUuid) {
        Address address = addressService.findAllAddressByCustomerUuid(customerUuid);
        return address != null ? ResponseEntity.ok().body(address) : ResponseEntity.badRequest().build();
    }

    @PostMapping("/{customerUuid}")
    public ResponseEntity<Address> createAddress(@RequestBody List<Address> newAddress, @PathVariable(name="customerUuid") String customerUuid) {
       boolean address = addressService.createAddress(newAddress, customerUuid);
        return address ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @PutMapping("/{addressUuid}")
    public ResponseEntity<Address> updateAddress(@RequestBody Address updatedAddress, @PathVariable(name="addressUuid") String addressUuid) {
        Address address = addressService.updateAddress(updatedAddress, addressUuid);
        return address != null ? ResponseEntity.ok().body(address) : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{addressUuid}")
    public ResponseEntity<Address> deleteAddress(@PathVariable(name="addressUuid") String addressUuid){
        boolean address = addressService.deleteAddressByUuid(addressUuid);
        return address ? ResponseEntity.noContent().build() : ResponseEntity.badRequest().build();
    }
}
