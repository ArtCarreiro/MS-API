package com.amc.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.amc.api.entities.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, String> {

    Address findByUuid(String uuid);

    @Modifying
    @Query(
        value = """
            UPDATE addresses a
            JOIN customers c ON a.customer_Uuid = c.uuid
            SET a.active = false, a.deleted = true
            WHERE c.uuid = :customerUuid
        """, nativeQuery = true)
    void deleteAddressByCustomerUuid(@Param("customerUuid") String customerUuid);

    boolean existsByCustomerUuidAndActiveTrueAndDeletedFalse(String customerUuid);

    @Query(
        value = """
            SELECT a.* 
            FROM addresses a
            JOIN customers c ON a.customer_Uuid = c.uuid
            WHERE c.uuid = :customerUuid AND a.active = true AND a.deleted = false
        """, nativeQuery = true)
    Optional<Address> findAddressByCustomerUuid(String customerUuid);

}
