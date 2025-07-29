package com.amc.api.Services;

import com.amc.api.Entities.Address;
import com.amc.api.Entities.User;
import com.amc.api.Repositories.AddressRepository;
import com.amc.api.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AddressService {

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    UserRepository userRepository;


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
    public Address createAddress(Address newAddress, String userUuid) {
        try {
            User user = userRepository.findByUuid(userUuid);
            if (user != null) {
                user.setAddress(newAddress);
                userRepository.save(user);
            }
            addressRepository.save(newAddress);
            return newAddress;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public Address updateAddress(Address updatedAddress, String addressUuid) {
        try {
            Address existingAddress = addressRepository.findByUuid(addressUuid);
            if (existingAddress != null) {
                existingAddress.setStreet(updatedAddress.getStreet());
                existingAddress.setCity(updatedAddress.getCity());
                existingAddress.setState(updatedAddress.getState());
                existingAddress.setZipCode(updatedAddress.getZipCode());
                existingAddress.setCountry(updatedAddress.getCountry());
                existingAddress.setNumber(updatedAddress.getNumber());
                existingAddress.setAddressComplement(updatedAddress.getAddressComplement());
                existingAddress.setNeighborhood(updatedAddress.getNeighborhood());
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
