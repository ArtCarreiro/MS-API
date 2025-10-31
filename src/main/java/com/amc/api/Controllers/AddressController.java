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

    @GetMapping("/{userUuid}")
    public ResponseEntity<Address> getAddressByUuid(@PathVariable(name="userUuid") String addressUuid) {
        Address address = addressService.findAddressByUuid(addressUuid);
        return address != null ? ResponseEntity.ok().body(address) : ResponseEntity.badRequest().build();
    }

    @GetMapping("all/{userUuid}")
    public ResponseEntity<Address> getAllAddressesByUserUuid(@PathVariable(name="userUuid") String userUuid) {
        Address address = addressService.findAllAddressByCustomerUuid(userUuid);
        return address != null ? ResponseEntity.ok().body(address) : ResponseEntity.badRequest().build();
    }

    @PostMapping("/{userUuid}")
    public ResponseEntity<Address> createAddress(@RequestBody List<Address> newAddress, @PathVariable(name="userUuid") String userUuid) {
       boolean address = addressService.createAddress(newAddress, userUuid);
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
