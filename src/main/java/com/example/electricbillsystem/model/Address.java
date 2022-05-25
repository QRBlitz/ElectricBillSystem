package com.example.electricbillsystem.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Address {

    @Id
    @Column(name = "address_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String city;
    private String street;
    @Column(name = "house_number")
    private String houseNum;

    @OneToMany
    @JoinColumn(name = "address_id")
    private List<Customer> customer;

    public Address() {
    }

    public Address(Integer id, String city, String street, String houseNum) {
        this.id = id;
        this.city = city;
        this.street = street;
        this.houseNum = houseNum;
    }
}
