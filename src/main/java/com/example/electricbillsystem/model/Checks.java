package com.example.electricbillsystem.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class Checks {

    @Id
    @Column(name = "check_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Date creationDate;
    private Double cashAdded;

    @ManyToOne
    @JoinColumn(name = "paypal_id")
    private Paypal paypals;
    @ManyToOne
    @JoinColumn(name = "credit_card_id")
    private CreditCard creditCards;
    @ManyToOne
    @JoinColumn(name = "debit_card_id")
    private DebitCard debitCards;

    public Checks() {
    }

    public Checks(Integer id, Date creationDate, Double cashAdded) {
        this.id = id;
        this.creationDate = creationDate;
        this.cashAdded = cashAdded;
    }

    public Checks(Integer id, Date creationDate, Double cashAdded, Paypal paypals, CreditCard creditCards, DebitCard debitCards) {
        this.id = id;
        this.creationDate = creationDate;
        this.cashAdded = cashAdded;
        this.paypals = paypals;
        this.creditCards = creditCards;
        this.debitCards = debitCards;
    }

}
