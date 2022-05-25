package com.example.electricbillsystem.repository;

import com.example.electricbillsystem.model.DebitCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface DebitCardRepo extends JpaRepository<DebitCard, Integer> {

    @Query("update DebitCard d set d.cash=?1 where d.id=?2")
    @Modifying
    void pay(Double money, Integer debitCardId);

}
