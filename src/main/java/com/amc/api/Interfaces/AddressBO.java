package com.amc.api.interfaces;

import com.amc.api.dto.AddressDTO;
import com.amc.api.entities.Address;

public interface AddressBO {
    
    Address createAddress(Address data);

    Address updateAddress(AddressDTO data, String uuid);

    boolean deleteAddress(String uuid);

    void validateAddress(Address data);
}
