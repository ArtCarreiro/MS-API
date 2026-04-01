package com.amc.api.Services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amc.api.DTO.AddressDTO;
import com.amc.api.Entities.Address;
import com.amc.api.Entities.Customer;
import com.amc.api.Interfaces.AddressBO;
import com.amc.api.Repositories.AddressRepository;
import com.amc.api.Repositories.CustomerRepository;
import com.amc.api.Utils.Exceptions;

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
    @Transactional
    public Address createAddress(Address data){
        try {
            Address newAddress = addressRepository.save(data);
            Customer customer = customerRepository.findByUuid(data.getCustomer().getUuid());
            customerRepository.save(customer);
            return newAddress;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public Address updateAddress(AddressDTO data, String uuid){
        try {
            Address address = addressRepository.findByUuid(uuid);
            mapper.map(data, address.getClass());
            return addressRepository.save(address);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public boolean deleteAddress(Address address){
        try {
            addressRepository.delete(address);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void validation(Address address) {
        if (address.getZipCode().length() < 8 )
            throw new Exceptions.InvalidRequestException("CEP não esta no formato correto.");
    }

}
