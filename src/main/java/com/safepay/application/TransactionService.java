package com.safepay.application;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.safepay.domain.RiskPolicy;
import com.safepay.domain.Transaction;

/**
 * Ring: application
 * Application service: orchestrates persistence and risk assessment for transactions.
 */
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final RiskPolicy riskPolicy;

    public TransactionService(TransactionRepository transactionRepository, RiskPolicy riskPolicy) {
        this.transactionRepository = transactionRepository;
        this.riskPolicy = riskPolicy;
    }

    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    public Optional<Transaction> findById(String id) {
        return transactionRepository.findById(id);
    }

    public String assessRisk(Transaction transaction) {
        return riskPolicy.assess(transaction);
    }
}
