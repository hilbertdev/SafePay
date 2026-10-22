package com.safepay.application;

import java.util.List;
import java.util.Optional;

import com.safepay.domain.Transaction;

/**
 * Ring: application
 * This interface is the PORT - the application ring owns it and the infrastructure ring implements it.
 */
public interface TransactionRepository {

    Transaction save(Transaction transaction);

    List<Transaction> findAll();

    Optional<Transaction> findById(String id);
}
