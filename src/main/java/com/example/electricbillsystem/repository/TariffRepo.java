package com.example.electricbillsystem.repository;

import com.example.electricbillsystem.model.Tariff;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TariffRepo extends JpaRepository<Tariff, Integer> {

}
