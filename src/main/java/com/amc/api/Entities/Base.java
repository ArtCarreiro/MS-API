package com.amc.api.Entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class Base {

    @Id
    @Column(updatable = false)
    private String uuid = UUID.randomUUID().toString();

    @CreatedDate
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date modified;

    private Boolean active = true;

    private Boolean deleted = false;

}
