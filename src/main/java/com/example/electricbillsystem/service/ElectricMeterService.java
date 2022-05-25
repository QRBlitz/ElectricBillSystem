package com.example.electricbillsystem.service;

import com.example.electricbillsystem.CustomException;
import com.example.electricbillsystem.model.Customer;
import com.example.electricbillsystem.model.ElectricMeter;
import com.example.electricbillsystem.repository.ElectricMeterRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ElectricMeterService {

    private final ElectricMeterRepo electricMeterRepo;

    @Autowired
    public ElectricMeterService(ElectricMeterRepo electricMeterRepo) {
        this.electricMeterRepo = electricMeterRepo;
    }

    public List<ElectricMeter> getElectricMeters() {
        return electricMeterRepo.findAll();
    }

    public void addElectricMeter(ElectricMeter electricMeter) throws CustomException {
        Customer customer = electricMeter.getCustomer();
        try {
            if (customer.getElectricMeter() == null) {
                electricMeterRepo.save(electricMeter);
            }
        } catch (Exception e) {
            throw new CustomException("This customer already have an electric meter, delete old one or edit it");
        }
    }

    public ElectricMeter getElectricMeter(Integer id) throws CustomException {
        ElectricMeter electricMeter;
        try {
            electricMeter = electricMeterRepo.findById(id).get();
        } catch (EmptyResultDataAccessException e) {
            throw new CustomException("There is no electric meter with such id");
        }
        return electricMeter;
    }

    public void deleteElectricMeter(Integer id) throws CustomException {
        ElectricMeter electricMeter = getElectricMeter(id);
        electricMeterRepo.delete(electricMeter);
    }

    public void resetCapacity(Integer id) throws CustomException {
        ElectricMeter electricMeter = getElectricMeter(id);
        electricMeter.setCapacity(0.0);
        electricMeterRepo.save(electricMeter);
    }

    public void addCapacity(Integer id, Double capacity) throws CustomException {
        ElectricMeter electricMeter = getElectricMeter(id);
        electricMeter.setCapacity(capacity + electricMeter.getCapacity());
        electricMeterRepo.save(electricMeter);
    }

}
