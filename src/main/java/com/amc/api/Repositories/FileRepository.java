package com.amc.api.Repositories;

import com.amc.api.Entities.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

    File findByUuid(String fileUuid);

}
