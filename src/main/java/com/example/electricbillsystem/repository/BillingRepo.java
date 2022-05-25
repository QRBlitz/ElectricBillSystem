package com.example.electricbillsystem.repository;

import com.example.electricbillsystem.model.Billing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BillingRepo extends JpaRepository<Billing, Integer> {

    @Query("select i.toPay from Invoice i where i.id = ?1")
    Double getBillAmount(Integer invoiceId);

}
