package com.example.electricbillsystem.controller;

import com.example.electricbillsystem.CustomException;
import com.example.electricbillsystem.model.Tariff;
import com.example.electricbillsystem.service.TariffService;
import com.example.electricbillsystem.validation.CustomValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tariff")
public class TariffController {

    private TariffService tariffService;
    private CustomValidator customValidator;

    @Autowired
    public TariffController(TariffService tariffService, CustomValidator customValidator) {
        this.tariffService = tariffService;
        this.customValidator = customValidator;
    }

    @GetMapping
    public ResponseEntity getTariffs() {
        return ResponseEntity.ok(tariffService.getTariffs());
    }

    @PostMapping("/add")
    public ResponseEntity addTariff(@RequestBody Tariff tariff) {
        List<String> errors = customValidator.validate(tariff);
        if (errors.isEmpty()) {
            tariffService.addTariff(tariff);
            return ResponseEntity.ok().body("Tariff has been added \n" + tariff);
        }
        return ResponseEntity.badRequest().body(errors);
    }

    @GetMapping("/get")
    public ResponseEntity getTariff(@RequestParam Integer id) {
        try {
            return ResponseEntity.ok(tariffService.getTariff(id));
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity deleteTariff(@RequestParam Integer id) throws CustomException {
        Tariff tariff;
        try {
            tariff = tariffService.getTariff(id);
            tariffService.deleteTariff(id);
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().body("Tariff has been deleted\n" + tariff);
    }

}
