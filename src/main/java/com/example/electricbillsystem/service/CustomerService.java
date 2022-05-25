package com.example.electricbillsystem.service;

import com.example.electricbillsystem.CustomException;
import com.example.electricbillsystem.model.*;
import com.example.electricbillsystem.repository.CustomerRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class CustomerService {

    private final CustomerRepo customerRepo;
    private final InvoiceService invoiceService;
    private final DebitCardService debitCardService;
    private final CreditCardService creditCardService;
    private final PaypalService paypalService;
    private final BillingService billingService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomerService(CustomerRepo customerRepo, InvoiceService invoiceService, DebitCardService debitCardService, CreditCardService creditCardService, PaypalService paypalService, BillingService billingService, PasswordEncoder passwordEncoder) {
        this.customerRepo = customerRepo;
        this.invoiceService = invoiceService;
        this.debitCardService = debitCardService;
        this.creditCardService = creditCardService;
        this.paypalService = paypalService;
        this.billingService = billingService;
        this.passwordEncoder = passwordEncoder;
    }

    public CustomerPagination getCustomersPage(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<Customer> customers = customerRepo.findAll(pageable);
        List<Customer> customerList = customers.getContent();

        CustomerPagination customerPagination = new CustomerPagination();
        customerPagination.setContent(customerList);
        customerPagination.setPageNumber(customers.getNumber());
        customerPagination.setPageSize(customers.getSize());
        customerPagination.setTotalBooks((int) customers.getTotalElements());
        customerPagination.setTotalPages(customers.getTotalPages());
        customerPagination.setLast(customers.isLast());

        return customerPagination;
    }

    public List<Customer> getCustomers() {
        return customerRepo.findAll();
    }

    public void addCustomer(Customer customer) throws CustomException {
        List<Customer> customers = getCustomers();
        for (Customer c : customers) {
            if (c.getLogin().equals(customer.getLogin())) {
                throw new CustomException("Customer with the same login already exists");
            }
        }
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customerRepo.save(customer);
    }

    public Customer getCustomer(Integer id) throws CustomException {
        Customer customer;
        try {
            customer = customerRepo.findById(id).get();
        } catch (EmptyResultDataAccessException e) {
            throw new CustomException("There is no customer with such id");
        }
        return customer;
    }

    public void editCustomer(Customer customer) throws CustomException {
        Customer oldCustomer = getCustomer(customer.getId());
        if (!oldCustomer.getLogin().equals(customer.getLogin())) {
            throw new CustomException("Customer's login can't be changed");
        }
        if (!oldCustomer.getPassword().equals(customer.getPassword())) {
            customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        }
        customerRepo.save(customer);
    }

    public void deleteCustomer(Integer id) throws CustomException {
        Customer customer = getCustomer(id);
        customerRepo.delete(customer);
    }

    public List<Customer> getCustomersAtAddress(Integer id) throws CustomException {
        List<Customer> addresses = customerRepo.getCustomersAtAddress(id);
        if (addresses.size() == 0) {
            throw new CustomException("Unfortunately, no one lives at this address");
        }
        return customerRepo.getCustomersAtAddress(id);
    }

    public void payDebitCard(Integer customerId, Integer invoiceId, Integer debitCardId, Double money) throws CustomException {
        List<Invoice> invoices = invoiceService.getUsersInvoices(customerId);
        Optional<Invoice> unPaid = invoices.stream().filter(x -> !x.isPaid()).findFirst();
        Invoice invoice = invoiceService.getInvoice(invoiceId);

        DebitCard customersCard = debitCardService.getDebitCard(debitCardId);

        if (!invoices.contains(invoice)) {
            throw new CustomException("This " + invoice + " isn't an account for this customer");
        }
        if (invoice.isPaid()) {
            throw new CustomException(invoice + " has already been paid");
        }
        if (invoice.getId() != unPaid.get().getId()) {
            throw new CustomException("There are later unpaid invoices, pay them first:\n" + unPaid.get());
        }
        if (customersCard.getCash() < money) {
            throw new CustomException("There are not enough funds on this card: " + customersCard + ", change the payment method or change the amount");
        }
        invoiceService.payInvoice(invoiceId);
        debitCardService.pay(customersCard.getCash() - money, debitCardId);
        billingService.addBilling(money, customerId, invoiceId);
    }

    public void payCreditCard(Integer customerId, Integer invoiceId, Integer creditCardId, Double money) throws CustomException {
        List<Invoice> invoices = invoiceService.getUsersInvoices(customerId);
        Optional<Invoice> unPaid = invoices.stream().filter(x -> !x.isPaid()).findFirst();
        Invoice invoice = invoiceService.getInvoice(invoiceId);

        CreditCard customersCard = creditCardService.getCreditCard(creditCardId);

        if (!invoices.contains(invoice)) {
            throw new CustomException("This " + invoice + " isn't an account for this customer");
        }
        if (invoice.isPaid()) {
            throw new CustomException(invoice + " has already been paid");
        }
        if (invoice.getId() != unPaid.get().getId()) {
            throw new CustomException("There are later unpaid invoices, pay them first:\n" + unPaid.get());
        }
        if (customersCard.getCash() < money) {
            throw new CustomException("There are not enough funds on this card: " + customersCard + ", change the payment method or change the amount");
        }
        invoiceService.payInvoice(invoiceId);
        creditCardService.pay(customersCard.getCash() - money, creditCardId);
        billingService.addBilling(money, customerId, invoiceId);
    }

    public void payPaypal(Integer customerId, Integer invoiceId, Integer paypalId, Double money) throws CustomException {
        List<Invoice> invoices = invoiceService.getUsersInvoices(customerId);
        Optional<Invoice> unPaid = invoices.stream().filter(x -> !x.isPaid()).findFirst();
        Invoice invoice = invoiceService.getInvoice(invoiceId);

        Paypal customersCard = paypalService.getPaypal(paypalId);

        if (!invoices.contains(invoice)) {
            throw new CustomException("This " + invoice + " isn't an account for this customer");
        }
        if (invoice.isPaid()) {
            throw new CustomException(invoice + " has already been paid");
        }
        if (invoice.getId() != unPaid.get().getId()) {
            throw new CustomException("There are later unpaid invoices, pay them first:\n" + unPaid.get());
        }
        if (customersCard.getCash() < money) {
            throw new CustomException("There are not enough funds on this card: " + customersCard + ", change the payment method or change the amount");
        }
        invoiceService.payInvoice(invoiceId);
        paypalService.pay(customersCard.getCash() - money, paypalId);
        billingService.addBilling(money, customerId, invoiceId);
    }

}
