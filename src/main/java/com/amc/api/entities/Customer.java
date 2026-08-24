package com.amc.api.entities;

import java.time.LocalDate;
import org.hibernate.annotations.SQLDelete;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customers")
@SQLDelete(sql = "UPDATE users SET deleted = true WHERE uuid=?")
public class Customer extends Base {

    @Column(name = "first_name", nullable = false)
    private String first_name;

    @Column(name = "last_name", nullable = false)
    private String last_name;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "phone", length = 12, nullable = false)
    private String phone;

    @Column(name = "newsletter")
    private Boolean newsletter = false;

    @Column(name = "document", nullable = false)
    private String document;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    @JsonIgnore
    private String password;


    @AssertTrue(message = "É necessário ser maior de idade")
    public boolean isOfLegalAge() {
    return birthDate != null &&
           birthDate.plusYears(18).isBefore(LocalDate.now());
    }

    public String encryptPassword(String password) {
        PasswordEncoder passwordEncoder = null;
        return passwordEncoder.encode(password);
    }
    
}
