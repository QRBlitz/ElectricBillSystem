package com.example.electricbillsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "electric_meter")
@Data
public class ElectricMeter {

    @Id
    @Column(name = "electric_meter_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    @PositiveOrZero(message = "Capacity can't be negative")
    private Double capacity;
    @Column(name = "company_name")
    private String companyName;

    @OneToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    @JsonIgnore
    private Customer customer;

    public ElectricMeter() {
    }

    public ElectricMeter(Integer id, Double capacity, String companyName) {
        this.id = id;
        this.capacity = capacity;
        this.companyName = companyName;
    }

    @Override
    public String toString() {
        return "ElectricMeter(" +
                "id=" + id +
                ", capacity=" + capacity +
                ", companyName='" + companyName +
                ')';
    }
}
