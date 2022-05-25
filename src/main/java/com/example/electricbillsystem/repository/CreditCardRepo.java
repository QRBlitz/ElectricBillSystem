package com.example.electricbillsystem.repository;

import com.example.electricbillsystem.model.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CreditCardRepo extends JpaRepository<CreditCard, Integer> {

    @Query("update CreditCard c set c.cash=?1 where c.id=?2")
    @Modifying
    void pay(Double money, Integer creditCardId);

}
