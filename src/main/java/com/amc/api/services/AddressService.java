package com.amc.api.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amc.api.dto.request.AddressRequestBodyDTO;
import com.amc.api.dto.response.AddressDTO;
import com.amc.api.entities.Address;
import com.amc.api.entities.Customer;
import com.amc.api.interfaces.AddressBO;
import com.amc.api.repositories.AddressRepository;
import com.amc.api.repositories.CustomerRepository;
import com.amc.api.utils.Exceptions.ResourceNotFoundException;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AddressService implements AddressBO {
    
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public AddressDTO getAddress(String customerUuid) {
        Address address = addressRepository.findAddressByCustomerUuidAndActiveTrueAndDeletedFalse(customerUuid)
            .orElseThrow(() -> new ResourceNotFoundException("Address not found with customer uuid: " + customerUuid));
        return mapper.map(address, AddressDTO.class);
    }
    
    @Override
    @Transactional
    public AddressDTO createAddress(AddressRequestBodyDTO addressData, String customerUuid){
        Customer customer = customerRepository.findByUuid(customerUuid)
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found with uuid: " + customerUuid));
        if (addressRepository.existsByCustomerUuidAndActiveTrueAndDeletedFalse(customerUuid))
            this.updateAddress(addressData, customerUuid);
        Address address = mapper.map(addressData, Address.class);
        address.setCustomer(customer);
        Address saved = addressRepository.save(address);
        log.info("Address created for customer uuid: {}", customerUuid);
        return mapper.map(saved, AddressDTO.class);
    }

    @Override
    @Transactional
    public AddressDTO updateAddress(AddressRequestBodyDTO addressData, String customerUuid){
        Address address = addressRepository.findAddressByCustomerUuidAndActiveTrueAndDeletedFalse(customerUuid)
            .orElseThrow(() -> new ResourceNotFoundException("Address not found with customer uuid: " + customerUuid));
        mapper.map(addressData, address);
        log.info("Address updated for customer uuid: {}", customerUuid);
        return mapper.map(address, AddressDTO.class);
    }

    @Override
    @Transactional
    public void deleteAddress(String customerUuid){
        Address address = addressRepository.findAddressByCustomerUuidAndActiveTrueAndDeletedFalse(customerUuid)
            .orElseThrow(() -> new ResourceNotFoundException("Address not found with customer uuid: " + customerUuid));
        addressRepository.deleteByUuid(address.getUuid());
        log.info("Address deleted for customer uuid: {}", customerUuid);
    }
}
