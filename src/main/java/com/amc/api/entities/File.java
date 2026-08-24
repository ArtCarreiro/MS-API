package com.amc.api.entities;

import org.hibernate.annotations.SQLDelete;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "files")
@SQLDelete(sql = "UPDATE files SET deleted = true WHERE uuid=?")
public class File extends Base {

    @NotNull
    private String name;

    @NotNull
    private String path;

    @ManyToOne
    @JoinColumn(name = "product_uuid")
    private Product product;
}
