package com.example.electricbillsystem.service;

import com.example.electricbillsystem.CustomException;
import com.example.electricbillsystem.model.Employee;
import com.example.electricbillsystem.repository.EmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EmployeeService {

    private final EmployeeRepo employeeRepo;

    @Autowired
    public EmployeeService(EmployeeRepo employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    public List<Employee> getEmployees() {
        return employeeRepo.findAll();
    }

    public void addEmployee(Employee employee) throws CustomException {
        List<Employee> employees = getEmployees();
        for (Employee c : employees) {
            if (c.getLogin().equals(employee.getLogin())) {
                throw new CustomException("Employee with the same login already exists");
            }
        }
        employeeRepo.save(employee);
    }

    public Employee getEmployee(Integer id) throws CustomException {
        Employee employee;
        try {
            employee = employeeRepo.findById(id).get();
        } catch (EmptyResultDataAccessException e) {
            throw new CustomException("There is no employee with such id");
        }
        return employee;
    }

    public void editEmployee(Employee employee) throws CustomException {
        Employee oldCustomer = getEmployee(employee.getId());
        if (!oldCustomer.getLogin().equals(employee.getLogin())) {
            throw new CustomException("Employee's login can't be changed");
        }
        employeeRepo.save(employee);
    }

    public void deleteEmployee(Integer id) throws CustomException {
        Employee employee = getEmployee(id);
        employeeRepo.delete(employee);
    }

}
