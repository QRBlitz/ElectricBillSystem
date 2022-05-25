package com.example.electricbillsystem.service;

import com.example.electricbillsystem.CustomException;
import com.example.electricbillsystem.model.Billing;
import com.example.electricbillsystem.model.ElectricMeter;
import com.example.electricbillsystem.model.Invoice;
import com.example.electricbillsystem.repository.InvoiceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class InvoiceService {

    private final InvoiceRepo invoiceRepo;
    private final ElectricMeterService electricMeterService;

    @Autowired
    public InvoiceService(InvoiceRepo invoiceRepo, ElectricMeterService electricMeterService) {
        this.invoiceRepo = invoiceRepo;
        this.electricMeterService = electricMeterService;
    }

    public List<Invoice> getInvoices() {
        return invoiceRepo.findAll();
    }

    public void addInvoice(Invoice invoice) throws CustomException {
        ElectricMeter electricMeter = invoiceRepo.getElectricMeter(invoice.getCustomer().getId());
        List<Billing> billings = invoiceRepo.getBilling(invoice.getCustomer().getId());
        billings = billings.stream().sorted(Comparator.comparing(Billing::getId).reversed()).collect(Collectors.toList());

        Double capacity, debt, excessPaid, surcharge, toPay;
        capacity = electricMeter.getCapacity();
        debt = billings.get(0).getBillAmount() - billings.get(0).getPaid();
        excessPaid = billings.get(0).getExcessPaid();
        surcharge = 0.0;
        if (debt > 0) {
            surcharge = debt * (invoice.getTariff().getPenaltyPercentage() / 100) + debt;
        } else {
            debt = 0.0;
        }
        toPay = capacity * invoice.getTariff().getPayRate() + surcharge - excessPaid;

        invoice.setCapacity(capacity);
        invoice.setDebt(debt);
        invoice.setExcess_paid(excessPaid);
        invoice.setSurcharge(surcharge);
        invoice.setToPay(toPay);

        invoiceRepo.save(invoice);
        electricMeterService.resetCapacity(electricMeter.getId());
    }

    public Invoice getInvoice(Integer id) throws CustomException {
        Invoice invoice;
        try {
            invoice = invoiceRepo.findById(id).get();
        } catch (EmptyResultDataAccessException e) {
            throw new CustomException("There is no invoice with such id");
        }
        return invoice;
    }

    public void deleteInvoice(Integer id) throws CustomException {
        Invoice invoice = getInvoice(id);
        invoiceRepo.delete(invoice);
    }

    public void payInvoice(Integer id) throws CustomException {
        Invoice invoice = getInvoice(id);
        invoiceRepo.payInvoice(invoice.getId());
    }

    public List<Invoice> getUsersInvoices(Integer id) throws CustomException {
        List<Invoice> invoices = invoiceRepo.getUsersInvoices(id);
        invoices = invoices.stream().sorted(Comparator.comparing(Invoice::getId)).collect(Collectors.toList());
        if (invoices.isEmpty()) {
            throw new CustomException("This customer doesn't have any invoices");
        }
        return invoices;
    }

    public List<Invoice> getUnpaidUsersInvoices(Integer id) throws CustomException {
        List<Invoice> invoices = invoiceRepo.getUnpaidUsersInvoices(id);
        invoices = invoices.stream().sorted(Comparator.comparing(Invoice::getId)).collect(Collectors.toList());
        if (invoices.isEmpty()) {
            throw new CustomException("This customer doesn't have any unpaid invoices");
        }
        return invoices;
    }

}
