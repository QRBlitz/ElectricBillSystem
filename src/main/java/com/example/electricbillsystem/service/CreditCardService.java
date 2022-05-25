package com.example.electricbillsystem.service;

import com.example.electricbillsystem.CustomException;
import com.example.electricbillsystem.jms.MessageSender;
import com.example.electricbillsystem.model.CreditCard;
import com.example.electricbillsystem.repository.CreditCardRepo;
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
public class CreditCardService {

    private final CreditCardRepo creditCardRepo;
    private final MessageSender messageSender;

    @Autowired
    public CreditCardService(CreditCardRepo creditCardRepo, MessageSender messageSender) {
        this.creditCardRepo = creditCardRepo;
        this.messageSender = messageSender;
    }

    public List<CreditCard> getCreditCards() {
        return creditCardRepo.findAll();
    }

    public void addCreditCard(CreditCard creditCard) throws CustomException {
        List<CreditCard> creditCards = getCreditCards();
        for (CreditCard c : creditCards) {
            if (c.getCardNumber().equals(creditCard.getCardNumber())) {
                throw new CustomException("Credit card with the same card number already exists");
            }
        }
        creditCardRepo.save(creditCard);
    }

    public CreditCard getCreditCard(Integer id) throws CustomException {
        CreditCard creditCard;
        try {
            creditCard = creditCardRepo.findById(id).get();
        } catch (EmptyResultDataAccessException e) {
            throw new CustomException("There is no credit card with such id");
        }
        return creditCard;
    }

    public void addCash(Double cash, Integer id) throws CustomException, JMSException {
        if (cash <= 0) {
            throw new CustomException("You cannot replenish funds on the card for an amount less than 1 tenge");
        }
        CreditCard creditCard = getCreditCard(id);
        messageSender.addCheck(null, creditCard, null, cash);
        creditCard.setCash(cash + creditCard.getCash());
        creditCardRepo.save(creditCard);
    }

    public void deleteCreditCard(Integer id) throws CustomException {
        CreditCard creditCard = getCreditCard(id);
        creditCardRepo.delete(creditCard);
    }

    public void pay(Double cash, Integer id) throws CustomException {
        CreditCard creditCard = getCreditCard(id);
        creditCardRepo.pay(cash, creditCard.getId());
    }

}
