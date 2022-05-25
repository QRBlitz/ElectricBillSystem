package com.example.electricbillsystem.controller;

import com.example.electricbillsystem.CustomException;
import com.example.electricbillsystem.model.Employee;
import com.example.electricbillsystem.service.EmployeeService;
import com.example.electricbillsystem.validation.CustomValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private EmployeeService employeeService;
    private CustomValidator customValidator;

    @Autowired
    public EmployeeController(EmployeeService employeeService, CustomValidator customValidator) {
        this.employeeService = employeeService;
        this.customValidator = customValidator;
    }

    @GetMapping
    public ResponseEntity getEmployees() {
        return ResponseEntity.ok(employeeService.getEmployees());
    }

    @PostMapping("/add")
    public ResponseEntity addEmployee(@RequestBody Employee employee) {
        List<String> errors = customValidator.validate(employee);
        if (errors.isEmpty()) {
            try {
                employeeService.addEmployee(employee);
            } catch (CustomException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
            return ResponseEntity.ok().body("Employee has been added \n" + employee);
        }
        return ResponseEntity.badRequest().body(errors);
    }

    @GetMapping("/get")
    public ResponseEntity getEmployee(@RequestParam Integer id) {
        try {
            return ResponseEntity.ok(employeeService.getEmployee(id));
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/edit")
    public ResponseEntity editEmployee(@RequestBody Employee employee) {
        List<String> errors = customValidator.validate(employee);
        if (errors.isEmpty()) {
            try {
                employeeService.editEmployee(employee);
            } catch (CustomException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
            return ResponseEntity.ok().body("Employee has been changed \n" + employee);
        }
        return ResponseEntity.badRequest().body(errors);
    }

    @DeleteMapping("/delete")
    public ResponseEntity deleteEmployee(@RequestParam Integer id) {
        Employee employee;
        try {
            employee = employeeService.getEmployee(id);
            employeeService.deleteEmployee(id);
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().body("Employee has been deleted\n" + employee);
    }

    @GetMapping("/converter")
    public ResponseEntity getStringToEmployee(@RequestParam Employee employee) {
        addEmployee(employee);
        return ResponseEntity.ok(employee);
    }

}
