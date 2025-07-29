package com.amc.api.Repositories;

import com.amc.api.Entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    Address findByUuid(String addressUuid);

    Address findAllByUserUuid(String userUuid);

}
