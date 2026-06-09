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
        Address address = addressRepository.findAddressByCustomerUuid(customerUuid)
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found with uuid: " + customerUuid));
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
        return mapper.map(saved, AddressDTO.class);
    }

    @Override
    @Transactional
    public AddressDTO updateAddress(AddressRequestBodyDTO addressData, String customerUuid){
        Address address = addressRepository.findAddressByCustomerUuid(customerUuid)
            .orElseThrow(() -> new ResourceNotFoundException("Address not found with customer uuid: " + customerUuid));
        mapper.map(addressData, address);
        return mapper.map(address, AddressDTO.class);
    }

    @Override
    @Transactional
    public void deleteAddress(String customerUuid){
        try {
            addressRepository.deleteAddressByCustomerUuid(customerUuid);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
