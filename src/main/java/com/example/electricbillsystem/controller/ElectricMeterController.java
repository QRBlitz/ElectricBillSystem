package com.example.electricbillsystem.controller;

import com.example.electricbillsystem.CustomException;
import com.example.electricbillsystem.model.Customer;
import com.example.electricbillsystem.model.ElectricMeter;
import com.example.electricbillsystem.service.CustomerService;
import com.example.electricbillsystem.service.ElectricMeterService;
import com.example.electricbillsystem.validation.CustomValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/electric")
public class ElectricMeterController {

    private ElectricMeterService electricMeterService;
    private CustomerService customerService;
    private CustomValidator customValidator;

    @Autowired
    public ElectricMeterController(ElectricMeterService electricMeterService, CustomerService customerService, CustomValidator customValidator) {
        this.electricMeterService = electricMeterService;
        this.customerService = customerService;
        this.customValidator = customValidator;
    }

    @GetMapping
    public ResponseEntity getElectricMeters() {
        return ResponseEntity.ok(electricMeterService.getElectricMeters());
    }

    @PostMapping("/add")
    public ResponseEntity addElectricMeter(@RequestBody ElectricMeter electricMeter, @RequestParam Integer customerId) {
        List<String> errors = customValidator.validate(electricMeter);
        if (errors.isEmpty()) {
            try {
                Customer customer = customerService.getCustomer(customerId);
                electricMeter.setCustomer(customer);
                electricMeterService.addElectricMeter(electricMeter);
            } catch (CustomException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
            return ResponseEntity.ok().body("Electric Meter has been added \n" + electricMeter);
        }
        return ResponseEntity.badRequest().body(errors);
    }

    @GetMapping("/get")
    public ResponseEntity getElectricMeter(@RequestParam Integer id) {
        try {
            return ResponseEntity.ok(electricMeterService.getElectricMeter(id));
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity deleteElectricMeter(@RequestParam Integer id) {
        ElectricMeter electricMeter;
        try {
            electricMeter = electricMeterService.getElectricMeter(id);
            electricMeterService.deleteElectricMeter(id);
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().body("Electric Meter has been deleted\n" + electricMeter);
    }

    @PutMapping("/reset")
    public ResponseEntity resetCapacity(@RequestParam Integer id) throws CustomException {
        electricMeterService.resetCapacity(id);
        ElectricMeter electricMeter = electricMeterService.getElectricMeter(id);
        return ResponseEntity.ok().body("Capacity on Electric Meter reset\n" + electricMeter);
    }

    @PutMapping("/addCapacity")
    public ResponseEntity addCapacity(@RequestParam Integer id, @RequestParam Double capacity) throws CustomException {
        electricMeterService.addCapacity(id, capacity);
        ElectricMeter electricMeter = electricMeterService.getElectricMeter(id);
        return ResponseEntity.ok().body("Capacity of used electricity on Electric Meter raised\n" + electricMeter);
    }

}
