package com.amc.api.Services;

import com.amc.api.Entities.Address;
import com.amc.api.Entities.User;
import com.amc.api.Repositories.AddressRepository;
import com.amc.api.Repositories.UserRepository;
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
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;


    public Address findAddressByUuid(String addressUuid) {
        try {
            return addressRepository.findByUuid(addressUuid);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Address findAllAddressByUserUuid(String userUuid) {
        try {
            return addressRepository.findAllByUserUuid(userUuid);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public Boolean createAddress(List<Address> newAddress, String userUuid) {
        try {
            User user = userRepository.findByUuid(userUuid);
            if (user != null) {
                user.setAddresses(newAddress);
                userRepository.save(user);
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
