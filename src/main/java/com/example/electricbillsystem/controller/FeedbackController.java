package com.example.electricbillsystem.controller;

import com.example.electricbillsystem.CustomException;
import com.example.electricbillsystem.model.Customer;
import com.example.electricbillsystem.model.Feedback;
import com.example.electricbillsystem.service.CustomerService;
import com.example.electricbillsystem.service.FeedbackService;
import com.example.electricbillsystem.validation.CustomValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    private FeedbackService feedbackService;
    private CustomerService customerService;
    private CustomValidator customValidator;

    @Autowired
    public FeedbackController(FeedbackService feedbackService, CustomerService customerService, CustomValidator customValidator) {
        this.feedbackService = feedbackService;
        this.customerService = customerService;
        this.customValidator = customValidator;
    }

    @GetMapping
    public ResponseEntity getFeedbacks() {
        return ResponseEntity.ok().body(feedbackService.getFeedbacks());
    }

    @PostMapping("/add")
    public ResponseEntity addFeedback(@RequestBody Feedback feedback, @RequestParam Integer customerId) {
        List<String> errors = customValidator.validate(feedback);
        if (errors.isEmpty()) {
            try {
                Customer customer = customerService.getCustomer(customerId);
                feedback.setCustomer(customer);
            } catch (CustomException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
            return ResponseEntity.ok().body(feedbackService.addFeedback(feedback));
        }
        return ResponseEntity.badRequest().body(errors);
    }

    @GetMapping("/get")
    public ResponseEntity getFeedback(@RequestParam Integer id) {
        try {
            return ResponseEntity.ok(feedbackService.getFeedback(id));
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getUsers")
    public ResponseEntity getUserFeedbacks(@RequestParam Integer id) {
        try {
            Customer customer = customerService.getCustomer(id);
            return ResponseEntity.ok(feedbackService.getUserFeedbacks(id));
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
