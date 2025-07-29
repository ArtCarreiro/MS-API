package com.amc.api.Entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(name="address")
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE address SET deleted = true WHERE uuid=?")
public class Address extends Base {

    @NotNull
    private String street;

    @NotNull
    private String neighborhood;

    @NotNull
    private Long zipCode;

    @NotNull
    private Integer number;

    private String addressComplement;

    @NotNull
    private String country;

    @NotNull
    private String city;

    @NotNull
    private String state;


    @OneToOne(mappedBy = "address", cascade = CascadeType.ALL)
    @JsonBackReference
    private User user;

}
