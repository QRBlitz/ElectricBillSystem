package com.example.electricbillsystem.controller;

import com.example.electricbillsystem.CustomException;
import com.example.electricbillsystem.model.Address;
import com.example.electricbillsystem.service.AddressService;
import com.example.electricbillsystem.validation.CustomValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressController {

    private AddressService addressService;
    private CustomValidator customValidator;

    @Autowired
    public AddressController(AddressService addressService, CustomValidator customValidator) {
        this.addressService = addressService;
        this.customValidator = customValidator;
    }

    @GetMapping
    public ResponseEntity getAddresses() {
        return ResponseEntity.ok(addressService.getAddresses());
    }

    @PostMapping("/add")
    public ResponseEntity addAddress(@RequestBody Address address) {
        List<String> errors = customValidator.validate(address);
        if (errors.isEmpty()) {
            try {
                addressService.addAddress(address);
            } catch (CustomException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
            return ResponseEntity.ok().body("Address has been added \n" + address);
        }
        return ResponseEntity.badRequest().body(errors);
    }

    @GetMapping("/get")
    public ResponseEntity getAddress(@RequestParam Integer id) {
        try {
            return ResponseEntity.ok(addressService.getAddress(id));
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity deleteAddress(@RequestParam Integer id) throws CustomException {
        Address address;
        try {
            address = addressService.getAddress(id);
            addressService.deleteAddress(id);
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().body("Address has been deleted\n" + address);
    }

}
