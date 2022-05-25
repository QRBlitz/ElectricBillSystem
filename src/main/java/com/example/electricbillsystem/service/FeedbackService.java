package com.example.electricbillsystem.service;

import com.example.electricbillsystem.CustomException;
import com.example.electricbillsystem.model.Feedback;
import com.example.electricbillsystem.repository.FeedbackRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FeedbackService {

    private FeedbackRepo feedbackRepo;

    @Autowired
    public FeedbackService(FeedbackRepo feedbackRepo) {
        this.feedbackRepo = feedbackRepo;
    }

    public List<Feedback> getFeedbacks() {
        return feedbackRepo.findAll();
    }

    public String addFeedback(Feedback feedback) {
        feedbackRepo.save(feedback);
        return "Thank you very much for your feedback, it is very important to us and we will try our best to make you feel comfortable.";
    }

    public Feedback getFeedback(Integer id) throws CustomException {
        Feedback feedback;
        try {
            feedback = feedbackRepo.findById(id).get();
        } catch (EmptyResultDataAccessException e) {
            throw new CustomException("There is no feedback with such id");
        }
        return feedback;
    }

    public List<String> getUserFeedbacks(Integer id) {
        List<Feedback> feedbacks = feedbackRepo.getUserFeedbacks(id);
        if (feedbacks.isEmpty()) {
            return Arrays.asList("Sadly, but this customer hasn't left any feedbacks yet");
        }
        feedbacks = feedbacks.stream().sorted(Comparator.comparing(Feedback::getId)).collect(Collectors.toList());
        List<String> data = new ArrayList<>();
        for (Feedback f : feedbacks) {
            data.add(f.toString());
        }
        return data;
    }

}
