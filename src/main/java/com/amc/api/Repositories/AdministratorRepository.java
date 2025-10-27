package com.amc.api.Repositories;

import com.amc.api.Entities.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Long> {

    Administrator findByUuid(String uuid);

}
