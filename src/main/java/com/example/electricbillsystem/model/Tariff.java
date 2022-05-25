package com.example.electricbillsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import java.util.List;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Data
public class Tariff {

    @Id
    @Column(name = "tariff_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "pay_rate")
    @Positive(message = "Pay rate can't be lower than 1%")
    private Double payRate;
    @Column(name = "penalty_percentage")
    @Positive(message = "Penalty percentage can't be lower than 1%")
    private Double penaltyPercentage;
    private String description;

    @OneToMany
    @JoinColumn(name = "invoice_id")
    @JsonIgnore
    private List<Invoice> invoice;

    public Tariff() {
    }

    public Tariff(Integer id, Double payRate, Double penaltyPercentage, String description) {
        this.id = id;
        this.payRate = payRate;
        this.penaltyPercentage = penaltyPercentage;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Tariff(" +
                "id=" + id +
                ", payRate=" + payRate +
                ", penaltyPercentage=" + penaltyPercentage +
                ", description='" + description +
                ')';
    }
}
