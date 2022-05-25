package com.example.electricbillsystem.service;

import com.example.electricbillsystem.CustomException;
import com.example.electricbillsystem.model.Tariff;
import com.example.electricbillsystem.repository.TariffRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional
public class TariffService {

    private TariffRepo tariffRepo;

    @Autowired
    public TariffService(TariffRepo tariffRepo) {
        this.tariffRepo = tariffRepo;
    }

    public List<Tariff> getTariffs() {
        return tariffRepo.findAll();
    }

    public void addTariff(Tariff tariff) {
        if (tariff.getDescription().equals("")) {
            log.info("Your description is empty, it is recommended to fill");
        }
        tariffRepo.save(tariff);
    }

    public Tariff getTariff(Integer id) throws CustomException {
        Tariff tariff;
        try {
            tariff = tariffRepo.findById(id).get();
        } catch (Exception e) {
            throw new CustomException("There is no tariff with such id");
        }
        return tariff;
    }

    public void deleteTariff(Integer id) throws CustomException {
        Tariff tariff = getTariff(id);
        tariffRepo.delete(tariff);
    }

}
