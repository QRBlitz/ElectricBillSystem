package com.example.electricbillsystem.service;

import com.example.electricbillsystem.CustomException;
import com.example.electricbillsystem.jms.MessageSender;
import com.example.electricbillsystem.model.DebitCard;
import com.example.electricbillsystem.repository.DebitCardRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.JMSException;
import java.util.List;

@Service
@Slf4j
@Transactional
public class DebitCardService {

    private final DebitCardRepo debitCardRepo;
    private final MessageSender messageSender;

    @Autowired
    public DebitCardService(DebitCardRepo debitCardRepo, MessageSender messageSender) {
        this.debitCardRepo = debitCardRepo;
        this.messageSender = messageSender;
    }

    public List<DebitCard> getDebitCards() {
        return debitCardRepo.findAll();
    }

    public void addDebitCard(DebitCard debitCard) throws CustomException {
        List<DebitCard> debitCards = getDebitCards();
        for (DebitCard d : debitCards) {
            if (d.getCardNumber().equals(debitCard.getCardNumber())) {
                throw new CustomException("Debit card with the same card number already exists");
            }
        }
        debitCardRepo.save(debitCard);
    }

    public DebitCard getDebitCard(Integer id) throws CustomException {
        DebitCard debitCard;
        try {
            debitCard = debitCardRepo.findById(id).get();
        } catch (EmptyResultDataAccessException e) {
            throw new CustomException("There is no debit card with such id");
        }
        return debitCard;
    }

    public void addCash(Double cash, Integer id) throws CustomException, JMSException {
        if (cash <= 0) {
            throw new CustomException("You cannot replenish funds on the card for an amount less than 1 tenge");
        }
        DebitCard debitCard = getDebitCard(id);
        messageSender.addCheck(debitCard, null, null, cash);
        debitCard.setCash(cash + debitCard.getCash());
        debitCardRepo.save(debitCard);
    }

    public void deleteDebitCard(Integer id) throws CustomException {
        DebitCard debitCard = getDebitCard(id);
        debitCardRepo.delete(debitCard);
    }

    public void pay(Double cash, Integer id) throws CustomException {
        DebitCard debitCard = getDebitCard(id);
        debitCardRepo.pay(cash, debitCard.getId());
    }

}
