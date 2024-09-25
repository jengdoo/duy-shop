package com.example.shopapp.repositories;

import com.example.shopapp.Model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepo extends JpaRepository<Payment,Long> {
    Payment findByTransactionId(String transactionId);
    Payment findByTxnRef(String txnRef);
}
