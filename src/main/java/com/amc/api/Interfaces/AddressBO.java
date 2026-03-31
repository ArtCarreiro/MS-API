package com.amc.api.Interfaces;

import com.amc.api.Entities.Address;

public interface AddressBO {

    void validation(Address address);
    
    Address createAddress(Address adress);

    boolean deleteAddress(Address address);
}
