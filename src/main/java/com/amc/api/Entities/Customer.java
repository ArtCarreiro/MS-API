package com.amc.api.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customers")
@SQLDelete(sql = "UPDATE users SET deleted = true WHERE uuid=?")
public class Customer extends Base {

    @NotNull
    private String first_name;

    @NotNull
    private String last_name;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date birthDate;

    @NotNull
    @Column(length = 12)
    private String phone;

    private Boolean newsletter;

    @NotNull
    private String document;

    private String gender;

    @OneToOne
    private User user;


}
