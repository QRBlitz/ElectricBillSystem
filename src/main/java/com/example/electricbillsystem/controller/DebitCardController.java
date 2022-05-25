package com.example.electricbillsystem.controller;

import com.example.electricbillsystem.CustomException;
import com.example.electricbillsystem.model.DebitCard;
import com.example.electricbillsystem.service.CustomerService;
import com.example.electricbillsystem.service.DebitCardService;
import com.example.electricbillsystem.validation.CustomValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.jms.JMSException;
import java.util.List;

@RestController
@RequestMapping("/debit")
public class DebitCardController {

    private DebitCardService debitCardService;
    private CustomerService customerService;
    private CustomValidator customValidator;

    @Autowired
    public DebitCardController(DebitCardService debitCardService, CustomerService customerService, CustomValidator customValidator) {
        this.debitCardService = debitCardService;
        this.customerService = customerService;
        this.customValidator = customValidator;
    }

    @GetMapping
    public ResponseEntity getDebitCards() {
        return ResponseEntity.ok().body(debitCardService.getDebitCards());
    }

    @PostMapping("/add")
    public ResponseEntity addDebitCard(@RequestBody DebitCard debitCard) {
        List<String> errors = customValidator.validate(debitCard);
        if (errors.isEmpty()) {
            try {
                debitCardService.addDebitCard(debitCard);
            } catch (CustomException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
            return ResponseEntity.ok().body("Debit card has been added \n" + debitCard);
        }
        return ResponseEntity.badRequest().body(errors);
    }

    @GetMapping("/get")
    public ResponseEntity getDebitCard(@RequestParam Integer id) {
        try {
            return ResponseEntity.ok(debitCardService.getDebitCard(id));
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/addCash")
    public ResponseEntity addCash(@RequestParam Double cash, @RequestParam Integer id) {
        try {
            debitCardService.addCash(cash, id);
        } catch (CustomException | JMSException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().body("Cash added");
    }

    @DeleteMapping("/delete")
    public ResponseEntity deleteDebitCard(@RequestParam Integer id) {
        DebitCard debitCard;
        try {
            debitCard = debitCardService.getDebitCard(id);
            debitCardService.deleteDebitCard(id);
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().body("Debit Card has been deleted\n" + debitCard);
    }

}
