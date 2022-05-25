package com.example.electricbillsystem.repository;

import com.example.electricbillsystem.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerRepo extends JpaRepository<Customer, Integer> {

    @Query("select c from Customer c where c.address.id = ?1")
    List<Customer> getCustomersAtAddress(Integer id);

    Customer findCustomerByLogin(String login);

}
