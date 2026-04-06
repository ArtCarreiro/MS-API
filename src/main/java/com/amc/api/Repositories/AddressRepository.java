package com.amc.api.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.amc.api.Entities.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, String> {

    Address findByUuid(String uuid);

    @Modifying
    @Query(
        value = """
            UPDATE customers 
            SET deleted = true, active = false
            WHERE uuid = :uuid
        """, nativeQuery = true)
    void deleteAddressByUuid(@Param("uuid") String uuid);

}
