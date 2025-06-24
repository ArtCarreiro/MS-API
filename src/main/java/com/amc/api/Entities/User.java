package com.amc.api.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name="users")
@MappedSuperclass
public class User extends Base {





}
