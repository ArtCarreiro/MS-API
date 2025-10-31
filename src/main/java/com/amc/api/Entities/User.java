package com.amc.api.Entities;

import com.amc.api.Enums.RolesEnum;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.SQLDelete;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Date;


@Getter
@Setter
@Entity
@Table(name="users")
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE users SET deleted = true WHERE uuid=?")
public class User extends Base {

    @NotNull
    @Email(message = "Email inválido")
    @Column(name = "email", nullable = false, length = 70)
    private String email;

    @NotNull
    @NotBlank(message = "Senha é obrigatório")
    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private RolesEnum role;

}
