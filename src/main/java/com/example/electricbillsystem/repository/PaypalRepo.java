package com.example.electricbillsystem.repository;

import com.example.electricbillsystem.model.Paypal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PaypalRepo extends JpaRepository<Paypal, Integer> {

    @Query("update Paypal p set p.cash=?1 where p.id=?2")
    @Modifying
    void pay(Double money, Integer paypalId);

}
