package com.example.electricbillsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Data
public class Invoice {

    @Id
    @Column(name = "invoice_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    @PositiveOrZero(message = "Capacity can't be negative")
    private Double capacity;
    @PositiveOrZero(message = "Debt can't be negative")
    private Double debt;
    @PositiveOrZero(message = "Excess paid can't be negative")
    private Double excess_paid;
    @PositiveOrZero(message = "Surcharge can't be negative")
    private Double surcharge;
    @Column(name = "to_pay")
    private Double toPay;
    private boolean paid;

    @ManyToOne
    @JoinColumn(name = "tariff_id", referencedColumnName = "tariff_id")
    @JsonIgnore
    private Tariff tariff;

    @OneToOne(mappedBy = "invoice")
    @JsonIgnore
    private Billing billing;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    private Customer customer;

    public Invoice() {
    }

    public Invoice(Integer id, Double capacity, Double debt, Double excess_paid, Double surcharge, Double toPay, boolean paid) {
        this.id = id;
        this.capacity = capacity;
        this.debt = debt;
        this.excess_paid = excess_paid;
        this.surcharge = surcharge;
        this.toPay = toPay;
        this.paid = paid;
    }

    @Override
    public String toString() {
        return "Invoice(" +
                "id=" + id +
                ", capacity=" + capacity +
                ", debt=" + debt +
                ", excess_paid=" + excess_paid +
                ", surcharge=" + surcharge +
                ", toPay=" + toPay +
                ", paid=" + paid +
                ", tariff=" + tariff +
                ')';
    }
}
