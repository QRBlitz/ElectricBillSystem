package com.example.electricbillsystem.controller;

import com.example.electricbillsystem.CustomException;
import com.example.electricbillsystem.model.Customer;
import com.example.electricbillsystem.model.Invoice;
import com.example.electricbillsystem.model.Tariff;
import com.example.electricbillsystem.service.CustomerService;
import com.example.electricbillsystem.service.InvoiceService;
import com.example.electricbillsystem.service.TariffService;
import com.example.electricbillsystem.validation.CustomValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {

    private InvoiceService invoiceService;
    private CustomerService customerService;
    private TariffService tariffService;
    private CustomValidator customValidator;

    @Autowired
    public InvoiceController(InvoiceService invoiceService, CustomerService customerService, TariffService tariffService, CustomValidator customValidator) {
        this.invoiceService = invoiceService;
        this.customerService = customerService;
        this.tariffService = tariffService;
        this.customValidator = customValidator;
    }

    @GetMapping
    public ResponseEntity getInvoices() {
        return ResponseEntity.ok(invoiceService.getInvoices());
    }

    @PostMapping("/add")
    public ResponseEntity addInvoice(@RequestBody Invoice invoice, @RequestParam Integer customerId, @RequestParam Integer tariffId) {
        List<String> errors = customValidator.validate(invoice);
        if (errors.isEmpty()) {
            try {
                Customer customer = customerService.getCustomer(customerId);
                Tariff tariff = tariffService.getTariff(tariffId);
                invoice.setCustomer(customer);
                invoice.setTariff(tariff);
                invoiceService.addInvoice(invoice);
            } catch (CustomException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
            return ResponseEntity.ok().body("Invoice has been added");
        }
        return ResponseEntity.badRequest().body(errors);
    }

    @GetMapping("/get")
    public ResponseEntity getInvoice(@RequestParam Integer id) {
        try {
            return ResponseEntity.ok(invoiceService.getInvoice(id));
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity deleteInvoice(@RequestParam Integer id) {
        Invoice invoice;
        try {
            invoice = invoiceService.getInvoice(id);
            invoiceService.deleteInvoice(id);
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().body("Invoice has been deleted\n" + invoice);
    }

    @GetMapping("/getUsersInvoices")
    public ResponseEntity getUsersInvoices(@RequestParam Integer id){
        try {
            Customer customer = customerService.getCustomer(id);
            return ResponseEntity.ok().body(invoiceService.getUsersInvoices(id));
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getUnpaidUsersInvoices")
    public ResponseEntity getUnpaidUsersInvoices(Integer id) {
        try {
            Customer customer = customerService.getCustomer(id);
            return ResponseEntity.ok().body(invoiceService.getUnpaidUsersInvoices(id));
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
