package com.example.electricbillsystem.controller;

import com.example.electricbillsystem.CustomException;
import com.example.electricbillsystem.model.CreditCard;
import com.example.electricbillsystem.service.CreditCardService;
import com.example.electricbillsystem.service.CustomerService;
import com.example.electricbillsystem.validation.CustomValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.jms.JMSException;
import java.util.List;

@RestController
@RequestMapping("/credit")
public class CreditCardController {

    private CreditCardService creditCardService;
    private CustomerService customerService;
    private CustomValidator customValidator;

    @Autowired
    public CreditCardController(CreditCardService creditCardService, CustomerService customerService, CustomValidator customValidator) {
        this.creditCardService = creditCardService;
        this.customerService = customerService;
        this.customValidator = customValidator;
    }

    @GetMapping
    public ResponseEntity getCreditCards() {
        return ResponseEntity.ok().body(creditCardService.getCreditCards());
    }

    @PostMapping("/add")
    public ResponseEntity addCreditCard(@RequestBody CreditCard creditCard) {
        List<String> errors = customValidator.validate(creditCard);
        if (errors.isEmpty()) {
            try {
                creditCardService.addCreditCard(creditCard);
            } catch (CustomException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
            return ResponseEntity.ok().body("Credit card has been added \n" + creditCard);
        }
        return ResponseEntity.badRequest().body(errors);
    }

    @GetMapping("/get")
    public ResponseEntity getCreditCard(Integer id) {
        try {
            return ResponseEntity.ok(creditCardService.getCreditCard(id));
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/addCash")
    public ResponseEntity addCash(@RequestParam Double cash,@RequestParam Integer id){
        try {
            creditCardService.addCash(cash, id);
        } catch (CustomException | JMSException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().body("Cash added");
    }

    @DeleteMapping("/delete")
    public ResponseEntity deleteCreditCard(Integer id) {
        CreditCard creditCard;
        try {
            creditCard = creditCardService.getCreditCard(id);
            creditCardService.deleteCreditCard(id);
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().body("Credit card has been deleted\n" + creditCard);
    }

}
