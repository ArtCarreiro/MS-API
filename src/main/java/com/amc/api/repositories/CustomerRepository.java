package com.amc.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.amc.api.entities.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

    Customer findByUuid(String uuid);

    Customer findByPhone(String phone);

    @Modifying
    @Query(
        value = """
            UPDATE customers 
            SET deleted = true, active = false
            WHERE uuid = :uuid
        """, nativeQuery = true)
    void deleteCustomerByUuid(@Param("uuid") String uuid);
}
