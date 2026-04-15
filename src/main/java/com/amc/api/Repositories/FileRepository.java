package com.amc.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.amc.api.entities.File;

@Repository
public interface FileRepository extends JpaRepository<File, String> {
    
    File findByUuid(String uuid);

}
