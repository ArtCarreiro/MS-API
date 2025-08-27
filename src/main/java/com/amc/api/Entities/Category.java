package com.amc.api.Entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="categories")
@SQLDelete(sql = "UPDATE categories SET deleted = true WHERE uuid=?")
public class Category extends Base {

    private String name;
    private String slug;

}
