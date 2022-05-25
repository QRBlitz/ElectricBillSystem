package com.example.electricbillsystem.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Billing {

    @Id
    @Column(name = "billing_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "payment_date")
    private Date paymentDate;
    @Column(name = "bill_amount")
    private Double billAmount;
    private Double paid;
    @Column(name = "excess_paid")
    private Double excessPaid;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToOne
    @JoinColumn(name = "invoice_id", referencedColumnName = "invoice_id")
    private Invoice invoice;

    public Billing() {
    }

    public Billing(Date paymentDate, Double billAmount, Double paid, Double excessPaid, Customer customer, Invoice invoice) {
        this.paymentDate = paymentDate;
        this.billAmount = billAmount;
        this.paid = paid;
        this.excessPaid = excessPaid;
        this.customer = customer;
        this.invoice = invoice;
    }
}
