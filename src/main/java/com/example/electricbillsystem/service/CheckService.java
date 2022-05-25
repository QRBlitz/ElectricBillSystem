package com.example.electricbillsystem.service;

import com.example.electricbillsystem.CustomException;
import com.example.electricbillsystem.model.Checks;
import com.example.electricbillsystem.repository.CheckRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional
public class CheckService {

    private final CheckRepo checkRepo;

    @Autowired
    public CheckService(CheckRepo checkRepo) {
        this.checkRepo = checkRepo;
    }

    public List<Checks> getChecks() {
        return checkRepo.findAll();
    }

    @JmsListener(destination = "addCheck")
    public void addCheck(Checks checks) throws CustomException {
        if (checks.getPaypals() == null && checks.getCreationDate() == null && checks.getDebitCards() == null) {
            throw new CustomException("No payment type has been selected");
        }
        checkRepo.save(checks);
        log.info(checks + " has been created");
    }

    public Checks getCheck(Integer id) throws CustomException {
        Checks checks;
        try {
            checks = checkRepo.findById(id).get();
        } catch (EmptyResultDataAccessException e) {
            throw new CustomException("There is no check with such id");
        }
        return checks;
    }

}
