package org.example.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "permissions")
public class Permission {

    @Id
    @Column(name = "name", nullable = false, unique = true)
    private String name;
}