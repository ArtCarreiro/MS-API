package com.amc.api.Interfaces;

import com.amc.api.DTO.AddressDTO;
import com.amc.api.Entities.Address;

public interface AddressBO {

    void validation(Address data);
    
    Address createAddress(Address data);

    Address updateAddress(AddressDTO data, String uuid);

    boolean deleteAddress(Address data);
}
