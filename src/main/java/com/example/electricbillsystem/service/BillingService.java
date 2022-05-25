package com.example.electricbillsystem.service;

import com.example.electricbillsystem.CustomException;
import com.example.electricbillsystem.model.Billing;
import com.example.electricbillsystem.model.Customer;
import com.example.electricbillsystem.model.Invoice;
import com.example.electricbillsystem.repository.BillingRepo;
import com.example.electricbillsystem.repository.CustomerRepo;
import com.example.electricbillsystem.repository.InvoiceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class BillingService {

    private final BillingRepo billingRepo;
    private final CustomerRepo customerRepo;
    private final InvoiceRepo invoiceRepo;

    @Autowired
    public BillingService(BillingRepo billingRepo, CustomerRepo customerRepo, InvoiceRepo invoiceRepo) {
        this.billingRepo = billingRepo;
        this.customerRepo = customerRepo;
        this.invoiceRepo = invoiceRepo;
    }

    public List<Billing> getBillings() {
        return billingRepo.findAll();
    }

    public void addBilling(Billing billing) {
        billingRepo.save(billing);
    }

    public void addBilling(Double paid, Integer customerId, Integer invoiceId) {
        Double billAmount = billingRepo.getBillAmount(invoiceId);
        Customer customer = customerRepo.findById(customerId).get();
        Invoice invoice = invoiceRepo.findById(invoiceId).get();
        Double excessPaid = paid - billAmount;
        if (paid - billAmount < 0) {
            excessPaid = 0.0;
        }
        Billing billing = new Billing(new Date(), billAmount, paid, excessPaid, customer, invoice);
        billingRepo.save(billing);
    }

    public Billing getBilling(Integer id) throws CustomException {
        Billing billing;
        try {
            billing = billingRepo.findById(id).get();
        } catch (EmptyResultDataAccessException e) {
            throw new CustomException("There is no billing with such id");
        }
        return billing;
    }

    public void deleteBilling(Integer id) throws CustomException {
        Billing billing = getBilling(id);
        billingRepo.delete(billing);
    }

}
