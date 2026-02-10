package com.amc.api.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customers")
@SQLDelete(sql = "UPDATE users SET deleted = true WHERE uuid=?")
public class Customer extends Base {

    private String first_name;
    private String last_name;
    private Date birth_date;
    private String phone;
    private Boolean newsletter;
    private String document;
    private String gender;

    @OneToOne
    private User user;


}
