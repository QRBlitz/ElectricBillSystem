package com.example.electricbillsystem.service;

import com.example.electricbillsystem.CustomException;
import com.example.electricbillsystem.jms.MessageSender;
import com.example.electricbillsystem.model.Paypal;
import com.example.electricbillsystem.repository.PaypalRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.JMSException;
import java.util.List;

@Service
@Transactional
public class PaypalService {

    private final PaypalRepo paypalRepo;
    private final MessageSender messageSender;

    @Autowired
    public PaypalService(PaypalRepo paypalRepo, MessageSender messageSender) {
        this.paypalRepo = paypalRepo;
        this.messageSender = messageSender;
    }

    public List<Paypal> getPaypals() {
        return paypalRepo.findAll();
    }

    public void addPaypal(Paypal paypal) throws CustomException {
        List<Paypal> paypals = getPaypals();
        for (Paypal p : paypals) {
            if (p.getPaypalEmail().equals(paypal.getPaypalEmail())) {
                throw new CustomException("Paypal account with the same email address already exists");
            }
        }
        paypalRepo.save(paypal);
    }

    public Paypal getPaypal(Integer id) throws CustomException {
        Paypal paypal;
        try {
            paypal = paypalRepo.findById(id).get();
        } catch (EmptyResultDataAccessException e) {
            throw new CustomException("There is no paypal account with such id");
        }
        return paypal;
    }

    public void addCash(Double cash, Integer id) throws CustomException, JMSException {
        if (cash <= 0) {
            throw new CustomException("You cannot replenish funds on the paypal account for an amount less than 1 tenge");
        }
        Paypal paypal = getPaypal(id);
        messageSender.addCheck(null, null, paypal, cash);
        paypal.setCash(cash + paypal.getCash());
        paypalRepo.save(paypal);
    }

    public void deletePaypal(Integer id) throws CustomException {
        Paypal paypal = getPaypal(id);
        paypalRepo.delete(paypal);
    }

    public void pay(Double cash, Integer id) throws CustomException {
        Paypal paypal = getPaypal(id);
        paypalRepo.pay(cash, paypal.getId());
    }

}
