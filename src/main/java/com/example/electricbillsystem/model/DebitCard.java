package com.example.electricbillsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "debit_card")
@Data
public class DebitCard {

    @Id
    @Column(name = "debit_card_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
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
    @AssertTrue(message = "Debit card should be verified")
    private boolean valid;

    @ManyToMany
    @JoinTable(
            name = "payment_details",
            joinColumns = @JoinColumn(name = "debit_card_id"),
            inverseJoinColumns = @JoinColumn(name = "customer_id")
    )
    @JsonIgnore
    private List<Customer> customers;

    @OneToMany
    @JoinColumn(name = "debit_card_id")
    @JsonIgnore
    private List<Checks> checks;

    public DebitCard() {
    }

    public DebitCard(Integer id, Long cardNumber, String cardholderName, Date expDate, Integer cvv, Double cash, boolean valid) {
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
        return "DebitCard(" +
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
