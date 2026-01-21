package com.amc.api.Entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(name="address")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private Customer customer;

}
