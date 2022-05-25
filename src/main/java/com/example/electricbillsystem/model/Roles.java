package com.example.electricbillsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Data
@Table(name = "roles")
public class Roles {

    @Id
    @Column(name = "role_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    private String name;

    @OneToMany
    @JoinColumn(name = "role_id")
    @JsonIgnore
    private List<Customer> customer;

    public Roles() {
    }

    public Roles(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
