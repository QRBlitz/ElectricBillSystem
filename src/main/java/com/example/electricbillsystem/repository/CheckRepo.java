package com.example.electricbillsystem.repository;

import com.example.electricbillsystem.model.Checks;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckRepo extends JpaRepository<Checks, Integer> {

}
