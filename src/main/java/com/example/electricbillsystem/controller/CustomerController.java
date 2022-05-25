package com.example.electricbillsystem.controller;

import com.example.electricbillsystem.CustomException;
import com.example.electricbillsystem.model.Address;
import com.example.electricbillsystem.model.Customer;
import com.example.electricbillsystem.service.AddressService;
import com.example.electricbillsystem.service.CustomerService;
import com.example.electricbillsystem.validation.CustomValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
@Slf4j
public class CustomerController {

    private CustomerService customerService;
    private AddressService addressService;
    private CustomValidator customValidator;

    @Autowired
    public CustomerController(CustomerService customerService, AddressService addressService, CustomValidator customValidator) {
        this.customerService = customerService;
        this.addressService = addressService;
        this.customValidator = customValidator;
    }

    @GetMapping("/page")
    public ResponseEntity getCustomersPage(@RequestParam(defaultValue = "0", required = false) int pageNumber,
                                           @RequestParam(defaultValue = "2", required = false) int pageSize) {
        return ResponseEntity.ok(customerService.getCustomersPage(pageNumber, pageSize));
    }

    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    @GetMapping()
    public ResponseEntity getCustomers() {
        return ResponseEntity.ok(customerService.getCustomers());
    }

    @PostMapping("/add")
    public ResponseEntity addCustomer(@RequestBody Customer customer, @RequestParam Integer addressId) {
        List<String> errors = customValidator.validate(customer);
        if (errors.isEmpty()) {
            try {
                Address address = addressService.getAddress(addressId);
                customer.setAddress(address);
                customerService.addCustomer(customer);
            } catch (CustomException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
            return ResponseEntity.ok().body("Customer has been added \n" + customer);
        }
        return ResponseEntity.badRequest().body(errors);
    }

    @GetMapping("/get")
    public ResponseEntity getCustomer(@RequestParam Integer id) {
        try {
            return ResponseEntity.ok(customerService.getCustomer(id));
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/edit")
    public ResponseEntity editCustomer(@RequestBody Customer customer) {
        List<String> errors = customValidator.validate(customer);
        if (errors.isEmpty()) {
            try {
                customerService.editCustomer(customer);
            } catch (CustomException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
            return ResponseEntity.ok().body("Customer has been changed \n" + customer);
        }
        return ResponseEntity.badRequest().body(errors);
    }

    @JmsListener(destination = "delete")
    @DeleteMapping("/delete")
    public ResponseEntity deleteCustomer(@RequestParam Integer customerId) {
        Customer customer;
        try {
            customer = customerService.getCustomer(customerId);
            customerService.deleteCustomer(customerId);
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().body("Customer has been deleted\n" + customer);
    }

//    @DeleteMapping("/jmsDelete")
//    @JmsListener(destination = "delete")
//    public void deleteCustomerJMS(Customer customer) throws CustomException {
//        List<Customer> customers = customerService.getCustomers();
//        if (customer.getId() == null) {
//            log.info("No customer has been sent");
//        }
//        if (!customers.contains(customer)) {
//            log.info("There is no such customer");
//        }
//        if (customers.contains(customer)) {
//            customerService.deleteCustomer(customer.getId());
//            log.info("Customer has been deleted\n" + customer);
//        }
//    }

    @GetMapping("getCustomersAtAddress")
    public ResponseEntity getCustomersAtAddress(@RequestParam Integer customerId) {
        try {
            return ResponseEntity.ok().body(customerService.getCustomersAtAddress(customerId));
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/payDebitCard")
    public ResponseEntity payDebitCard(@RequestParam Integer customerId, @RequestParam Integer invoiceId, @RequestParam Integer debitCardId, @RequestParam Double money) {
        try {
            customerService.payDebitCard(customerId, invoiceId, debitCardId, money);
            return ResponseEntity.ok().body("Invoice has been paid");
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/payCreditCard")
    public ResponseEntity payCreditCard(@RequestParam Integer customerId, @RequestParam Integer invoiceId, @RequestParam Integer creditCardId, @RequestParam Double money) {
        try {
            customerService.payCreditCard(customerId, invoiceId, creditCardId, money);
            return ResponseEntity.ok().body("Invoice has been paid");
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/payPaypal")
    public ResponseEntity payPaypal(@RequestParam Integer customerId, @RequestParam Integer invoiceId, @RequestParam Integer paypalId, @RequestParam Double money) {
        try {
            customerService.payPaypal(customerId, invoiceId, paypalId, money);
            return ResponseEntity.ok().body("Invoice has been paid");
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
