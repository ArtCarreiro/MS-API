package com.amc.api.Services;

import com.amc.api.Entities.Address;
import com.amc.api.Entities.Customer;
import com.amc.api.Repositories.AddressRepository;
import com.amc.api.Repositories.CustomerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ModelMapper modelMapper;


    public Address findAddressByUuid(String addressUuid) {
        try {
            return addressRepository.findByUuid(addressUuid);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Address findAllAddressByCustomerUuid(String customerUuid) {
        try {
            return addressRepository.findAllByCustomerUuid(customerUuid);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public Boolean createAddress(List<Address> newAddress, String customerUuid) {
        try {
            Customer customer = customerRepository.findByUuid(customerUuid);
            if (customer != null) {
                customer.setAddresses(newAddress);
                customerRepository.save(customer);
            }
            addressRepository.saveAll(newAddress);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public Address updateAddress(Address updatedAddress, String addressUuid) {
        modelMapper.typeMap(Address.class, Address.class)
                .addMappings(mapper -> mapper.skip(Address::setUuid));
        try {
            Address existingAddress = addressRepository.findByUuid(addressUuid);
            if (existingAddress != null) {
                modelMapper.map(updatedAddress, existingAddress);
                return addressRepository.save(existingAddress);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Transactional
    public boolean deleteAddressByUuid(String addressUuid) {
        try {
           Address address = addressRepository.findByUuid(addressUuid);
           if (address != null) {
               addressRepository.delete(address);
               return true;
           } else
               return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
