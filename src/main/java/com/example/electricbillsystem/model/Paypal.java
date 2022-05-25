package com.example.electricbillsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Data
public class Paypal {

    @Id
    @Column(name = "paypal_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "paypal_email")
    private String paypalEmail;
    @Column(name = "paypal_password")
    private String paypalPassword;
    @PositiveOrZero(message = "Cash can't be negative")
    private Double cash;
    @AssertTrue(message = "Paypal account should be verified")
    private boolean valid;

    @ManyToMany
    @JoinTable(
            name = "payment_details",
            joinColumns = @JoinColumn(name = "paypal_id"),
            inverseJoinColumns = @JoinColumn(name = "customer_id")
    )
    @JsonIgnore
    private List<Customer> customers;

    @OneToMany
    @JoinColumn(name = "paypal_id")
    @JsonIgnore
    private List<Checks> checks;

    public Paypal() {
    }

    public Paypal(Integer id, String paypalEmail, String paypalPassword, Double cash, boolean valid) {
        this.id = id;
        this.paypalEmail = paypalEmail;
        this.paypalPassword = paypalPassword;
        this.cash = cash;
        this.valid = valid;
    }

    @Override
    public String toString() {
        return "Paypal(" +
                "id=" + id +
                ", paypalEmail='" + paypalEmail +
                ", paypalPassword='" + paypalPassword +
                ", cash=" + cash +
                ", valid=" + valid +
                ", customers=" + customers +
                ')';
    }
}
