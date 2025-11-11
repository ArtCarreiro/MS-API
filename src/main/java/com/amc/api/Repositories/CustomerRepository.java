package com.amc.api.Repositories;

import com.amc.api.Entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

    Customer findByUuid(String uuid);

}
