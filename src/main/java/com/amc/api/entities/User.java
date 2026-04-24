package com.amc.api.entities;

import org.hibernate.annotations.SQLDelete;

import com.amc.api.enums.UserRoleEnum;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name="users")
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
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotNull
    @NotBlank(message = "Role é obrigatório")
    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;
}
