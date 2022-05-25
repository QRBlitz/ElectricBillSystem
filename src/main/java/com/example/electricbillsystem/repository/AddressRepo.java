package com.example.electricbillsystem.repository;

import com.example.electricbillsystem.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepo extends JpaRepository<Address, Integer> {

}
