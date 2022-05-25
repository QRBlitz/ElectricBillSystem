package com.example.electricbillsystem.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "credit_card")
@Data
public class CreditCard {

    @Id
    @Column(name = "credit_card_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "card_number")
    private Long cardNumber;
    @Column(name = "cardholder_name")
    private String cardholderName;
    @Column(name = "exp_date")
    @Future(message = "Expiration date should be in the future")
    private Date expDate;
    @Min(value = 100, message = "CVV code should contain 3 digits")
    @Max(value = 999, message = "CVV code should contain 3 digits")
    private Integer cvv;
    @PositiveOrZero(message = "Cash can't be negative")
    private Double cash;
    @AssertTrue(message = "Credit card should be verified")
    private boolean valid;

    @ManyToMany
    @JoinTable(
            name = "payment_details",
            joinColumns = @JoinColumn(name = "credit_card_id"),
            inverseJoinColumns = @JoinColumn(name = "customer_id")
    )
    private List<Customer> customers;

    @OneToMany
    @JoinColumn(name = "credit_card_id")
    private List<Checks> checks;

    public CreditCard() {
    }

    public CreditCard(Integer id, Long cardNumber, String cardholderName, Date expDate, Integer cvv, Double cash, boolean valid) {
        this.id = id;
        this.cardNumber = cardNumber;
        this.cardholderName = cardholderName;
        this.expDate = expDate;
        this.cvv = cvv;
        this.cash = cash;
        this.valid = valid;
    }

    @Override
    public String toString() {
        return "CreditCard(" +
                "id=" + id +
                ", cardNumber=" + cardNumber +
                ", cardholderName=" + cardholderName +
                ", expDate=" + expDate +
                ", cvv=" + cvv +
                ", cash=" + cash +
                ", valid=" + valid +
                ')';
    }
}
