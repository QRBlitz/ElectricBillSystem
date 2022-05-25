package com.example.electricbillsystem.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Positive;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Data
public class Employee {

    @Id
    @Column(name = "employee_id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String surname;
    private String login;
    @Length(min = 6, message = "Password must contain at least 6 elements")
    private String password;
    @Positive(message = "Salary can't be lower than 1")
    private Double salary;

    public Employee() {
    }

    public Employee(Integer id, String name, String surname, String login, String password, Double salary) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.password = password;
        this.salary = salary;
    }
}
