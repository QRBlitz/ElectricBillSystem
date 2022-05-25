package com.example.electricbillsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Data
public class Customer {

    @Id
    @Column(name = "customer_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String surname;
    private String login;
    @Length(min = 5, message = "Password must contain at least 6 elements")
    private String password;

    @ManyToOne
    @JoinColumn(name = "address_id", referencedColumnName = "address_id")
    @JsonIgnore
    private Address address;

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "role_id")
    @JsonIgnore
    private Roles roles;

    @OneToOne(mappedBy = "customer")
    private ElectricMeter electricMeter;

    @OneToMany(mappedBy = "customer")
    private List<Feedback> feedback;

    @OneToMany(mappedBy = "customer")
    @JsonIgnore
    private List<Billing> billings;

    @OneToMany
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    private List<Invoice> invoices;

    @ManyToMany(mappedBy = "customers")
    @JsonIgnore
    private List<DebitCard> debitCards;

    @ManyToMany(mappedBy = "customers")
    @JsonIgnore
    private List<CreditCard> creditCards;

    @ManyToMany(mappedBy = "customers")
    @JsonIgnore
    private List<Paypal> paypals;

    public Customer() {
    }

    public Customer(Integer id, String name, String surname, String login, String password) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.password = password;
    }

    public Customer(String name, String surname, String login, String password, Address address, Roles roles) {
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.password = password;
        this.address = address;
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "Customer(id=" + id +", name=" + name + ", surname=" + surname + ", login=" + login + ", password=" + password + ")";
    }
}
