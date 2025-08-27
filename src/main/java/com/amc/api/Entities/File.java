package com.amc.api.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="files")
@SQLDelete(sql = "UPDATE files SET deleted = true WHERE uuid=?")
public class File extends Base {

    private String name;
    private String pathUrl;

}
