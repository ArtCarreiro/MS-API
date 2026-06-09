package com.amc.api.interfaces;

import com.amc.api.dto.request.AddressRequestBodyDTO;
import com.amc.api.dto.response.AddressDTO;

public interface AddressBO {

    AddressDTO getAddress(String customerUuid);
    
    AddressDTO createAddress(AddressRequestBodyDTO addressData, String customerUuid);

    AddressDTO updateAddress(AddressRequestBodyDTO addressData, String customerUuid);

    void deleteAddress(String customerUuid);
}
