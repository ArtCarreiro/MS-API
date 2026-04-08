package com.amc.api.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amc.api.dto.AddressDTO;
import com.amc.api.entities.Address;
import com.amc.api.entities.Customer;
import com.amc.api.interfaces.AddressBO;
import com.amc.api.repositories.AddressRepository;
import com.amc.api.repositories.CustomerRepository;
import com.amc.api.utils.Exceptions;

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
            if (address == null)
                throw new RuntimeException("Endereço não encontrado");
            mapper.map(data, address.getClass());
            return addressRepository.save(address);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public boolean deleteAddress(String uuid){
        try {
            addressRepository.deleteAddressByUuid(uuid);
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
