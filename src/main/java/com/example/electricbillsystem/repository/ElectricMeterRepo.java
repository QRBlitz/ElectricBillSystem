package com.example.electricbillsystem.repository;

import com.example.electricbillsystem.model.ElectricMeter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ElectricMeterRepo extends JpaRepository<ElectricMeter, Integer> {

}
