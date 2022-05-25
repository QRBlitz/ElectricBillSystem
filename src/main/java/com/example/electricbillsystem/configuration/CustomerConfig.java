package com.example.electricbillsystem.configuration;

import com.example.electricbillsystem.CustomException;
import com.example.electricbillsystem.model.Address;
import com.example.electricbillsystem.model.Customer;
import com.example.electricbillsystem.model.Roles;
import com.example.electricbillsystem.repository.RolesRepo;
import com.example.electricbillsystem.service.AddressService;
import com.example.electricbillsystem.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;

@Configuration
@Slf4j
@Scope("singleton")
public class CustomerConfig {

    private CustomerService customerService;
    private AddressService addressService;
    private RolesRepo rolesRepo;

    @Autowired
    public CustomerConfig(CustomerService customerService, AddressService addressService, RolesRepo rolesRepo) {
        this.customerService = customerService;
        this.addressService = addressService;
        this.rolesRepo = rolesRepo;
    }

    @Bean("addCustomer")
    @Lazy
    public void addCustomer() throws CustomException {
        Address address = addressService.getAddresses().get(0);
        Roles role = rolesRepo.findAll().get(0);
        Customer customer = new Customer("Adilet", "Salkanov", "salkanov", "qwerty", address, role);
        customerService.addCustomer(customer);
        System.out.println("Customer has been added");
    }

    @Bean("editCustomer")
    @DependsOn("addCustomer")
    public void editCustomer() throws CustomException {
        Customer customer = customerService.getCustomer(1);
        customer.setSurname("Bolta");
        customerService.editCustomer(customer);
        System.out.println("Customer has been changed");
    }

    @Bean("deleteCustomer")
    @DependsOn("editCustomer")
    public Customer deleteCustomer() throws CustomException {
        Customer customer = customerService.getCustomer(4);
        customerService.deleteCustomer(4);
        System.out.println("Customer with 4th id has been deleted");
        return customer;
    }

}
