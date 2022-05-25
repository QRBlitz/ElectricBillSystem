package com.example.electricbillsystem.controller;

import com.example.electricbillsystem.CustomException;
import com.example.electricbillsystem.model.Paypal;
import com.example.electricbillsystem.service.CustomerService;
import com.example.electricbillsystem.service.PaypalService;
import com.example.electricbillsystem.validation.CustomValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.jms.JMSException;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/paypal")
public class PaypalController {

    private PaypalService paypalService;
    private CustomerService customerService;
    private CustomValidator customValidator;

    @Autowired
    public PaypalController(PaypalService paypalService, CustomerService customerService, CustomValidator customValidator) {
        this.paypalService = paypalService;
        this.customerService = customerService;
        this.customValidator = customValidator;
    }

    @GetMapping
    public ResponseEntity getPaypal() {
        return ResponseEntity.ok().body(paypalService.getPaypals());
    }

    @PostMapping("/add")
    public ResponseEntity add(@RequestBody Paypal paypal) {
        List<String> errors = customValidator.validate(paypal);
        if (errors.isEmpty()) {
            try {
                paypalService.addPaypal(paypal);
            } catch (CustomException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
            return ResponseEntity.ok().body("Paypal has been added \n" + paypal);
        }
        return ResponseEntity.badRequest().body(errors);
    }

    @GetMapping("/get")
    public ResponseEntity getPaypal(@RequestParam Integer id) {
        try {
            return ResponseEntity.ok(paypalService.getPaypal(id));
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/addCash")
    public ResponseEntity addCash(@RequestParam Double cash, @RequestParam Integer id) {
        try {
            paypalService.addCash(cash, id);
        } catch (CustomException | JMSException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().body("Cash added");
    }

    @DeleteMapping("/delete")
    public ResponseEntity deletePaypal(@RequestParam Integer id) {
        Paypal paypal;
        try {
            paypal = paypalService.getPaypal(id);
            paypalService.deletePaypal(id);
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().body("Paypal account has been deleted\n" + paypal);
    }

}
