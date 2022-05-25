package com.example.electricbillsystem.repository;

import com.example.electricbillsystem.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FeedbackRepo extends JpaRepository<Feedback, Integer> {

    @Query("select f from Feedback f where f.customer.id=?1")
    List<Feedback> getUserFeedbacks(Integer id);

}
