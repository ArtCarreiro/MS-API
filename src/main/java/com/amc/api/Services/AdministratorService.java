package com.amc.api.Services;

import com.amc.api.Entities.Administrator;
import com.amc.api.Repositories.AdministratorRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdministratorService {

    @Autowired
    private AdministratorRepository administratorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Transactional
    public boolean updateAdministrator(String administratorUuid, String newAdministrator) {
        Administrator administrator = administratorRepository.findByUuid(administratorUuid);
        if (administrator == null)
            return false;
        administrator.setUsername(newAdministrator);
        administratorRepository.save(administrator);
        return true;
    }


    public Administrator createAdministrator(Administrator administrator) {
        if (administrator != null) {
            String passwordEncoded = passwordEncoder.encode(administrator.getPassword());
            administrator.setPassword(passwordEncoded);
            administratorRepository.save(administrator);
            return administrator;
        }
        return null;
    }

    @Transactional
    public boolean changePassword(String administratorUuid, String newPassword) {
        Administrator administrator = administratorRepository.findByUuid(administratorUuid);
        if (administrator != null){
            administrator.setPassword(passwordEncoder.encode(newPassword));
            administratorRepository.save(administrator);
        }
        return true;
    }

    @Transactional
    public boolean deleteAdministrator(String administratorUuid) {
        try {
            Administrator administrator = administratorRepository.findByUuid(administratorUuid);
            if (administrator == null)
                return false;
            administratorRepository.delete(administrator);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
