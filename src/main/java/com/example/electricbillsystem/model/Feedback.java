package com.example.electricbillsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.PastOrPresent;
import java.util.Date;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Data
public class Feedback {

    @Id
    @Column(name = "feedback_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "feedback_text")
    private String feedbackText;
    @Column(name = "feedback_date")
    @PastOrPresent(message = "Date of feedback can't be in future")
    private Date feedbackDate;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    private Customer customer;

    public Feedback() {
    }

    public Feedback(Integer id, String feedbackText, Date feedbackDate) {
        this.id = id;
        this.feedbackText = feedbackText;
        this.feedbackDate = feedbackDate;
    }

    @Override
    public String toString() {
        return "Feedback(" +
                "id=" + id +
                ", feedbackText='" + feedbackText +
                ", feedbackDate=" + feedbackDate +
                ')';
    }
}
