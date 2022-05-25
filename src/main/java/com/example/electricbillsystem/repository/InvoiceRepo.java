package com.example.electricbillsystem.repository;

import com.example.electricbillsystem.model.Billing;
import com.example.electricbillsystem.model.ElectricMeter;
import com.example.electricbillsystem.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InvoiceRepo extends JpaRepository<Invoice, Integer> {

    @Query("update Invoice i set i.paid=true where i.id=?1")
    @Modifying
    void payInvoice(Integer invoiceId);

    @Query("select i from Invoice i where i.customer.id=?1")
    List<Invoice> getUsersInvoices(Integer id);

    @Query("select i from Invoice i where i.customer.id=?1 and i.paid=false")
    List<Invoice> getUnpaidUsersInvoices(Integer id);

    @Query("select e from ElectricMeter e where e.customer.id = ?1")
    ElectricMeter getElectricMeter(Integer id);

    @Query("select b from Billing b where b.customer.id = ?1")
    List<Billing> getBilling(Integer id);

}
