package com.amc.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.amc.api.entities.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, String> {

    Optional<Address> findAddressByCustomerUuidAndActiveTrueAndDeletedFalse(String customerUuid);

    void deleteByUuid(String uuid);

    boolean existsByCustomerUuidAndActiveTrueAndDeletedFalse(String customerUuid);
}
