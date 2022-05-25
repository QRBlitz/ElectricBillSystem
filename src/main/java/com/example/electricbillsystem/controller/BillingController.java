package com.example.electricbillsystem.controller;

import com.example.electricbillsystem.CustomException;
import com.example.electricbillsystem.model.Billing;
import com.example.electricbillsystem.model.Customer;
import com.example.electricbillsystem.model.Invoice;
import com.example.electricbillsystem.service.BillingService;
import com.example.electricbillsystem.service.CustomerService;
import com.example.electricbillsystem.service.InvoiceService;
import com.example.electricbillsystem.validation.CustomValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/billing")
public class BillingController {

    private BillingService billingService;
    private CustomerService customerService;
    private InvoiceService invoiceService;
    private CustomValidator customValidator;

    @Autowired
    public BillingController(BillingService billingService, CustomerService customerService, InvoiceService invoiceService, CustomValidator customValidator) {
        this.billingService = billingService;
        this.customerService = customerService;
        this.invoiceService = invoiceService;
        this.customValidator = customValidator;
    }

    @GetMapping
    public ResponseEntity getBillings() {
        return ResponseEntity.ok(billingService.getBillings());
    }

    @PostMapping("/add")
    public ResponseEntity addBilling(@RequestBody Billing billing, @RequestParam Integer customerId, @RequestParam Integer invoiceId) {
        List<String> errors = customValidator.validate(billing);
        if (errors.isEmpty()) {
            try {
                Customer customer = customerService.getCustomer(customerId);
                Invoice invoice = invoiceService.getInvoice(invoiceId);
                billing.setCustomer(customer);
                billing.setInvoice(invoice);
                billingService.addBilling(billing);
            } catch (CustomException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
            return ResponseEntity.ok().body("Billing has been added \n" + billing);
        }
        return ResponseEntity.badRequest().body(errors);
    }

    @GetMapping("/get")
    public ResponseEntity getBilling(@RequestParam Integer id) {
        try {
            return ResponseEntity.ok(billingService.getBilling(id));
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity deleteBilling(@RequestParam Integer id) {
        Billing billing;
        try {
            billing = billingService.getBilling(id);
            billingService.deleteBilling(id);
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().body("Billing has been deleted\n" + billing);
    }

}
