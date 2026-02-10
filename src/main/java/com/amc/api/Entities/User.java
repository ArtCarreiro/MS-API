package com.amc.api.Entities;

import com.amc.api.Enums.UserRoleEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.SQLDelete;

@Getter
@Setter
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
