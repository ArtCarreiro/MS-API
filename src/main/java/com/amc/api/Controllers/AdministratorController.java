package com.amc.api.Controllers;

import com.amc.api.Entities.Administrator;
import com.amc.api.Services.AdministratorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/administrator")
public class AdministratorController {

    @Autowired
    AdministratorService administratorService;


    @PostMapping
    public ResponseEntity<Administrator> createAdministrator(@Valid @RequestBody Administrator newAdministrator) {
        Administrator administrator = administratorService.createAdministrator(newAdministrator);
        return administrator != null ? ResponseEntity.ok().body(administrator) : ResponseEntity.badRequest().build();
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Administrator> updateAdministrator(@Valid @PathVariable(name = "uuid") String uuid, @RequestBody String newUsername) {
        boolean updateAdministrator = administratorService.updateAdministrator(uuid, newUsername);
        return updateAdministrator ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @PutMapping("/{uuid}/change-password")
    public ResponseEntity<?> changePassword(@PathVariable("uuid") String administratorUuid, @RequestBody String newPassword) {
        boolean passwordChanged = administratorService.changePassword(administratorUuid, newPassword);
        return passwordChanged ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Administrator> deleteAdministrator(@Valid @PathVariable(name = "uuid") String administratorUuid) {
        boolean deleteAdministrator = administratorService.deleteAdministrator(administratorUuid);
        return deleteAdministrator ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
