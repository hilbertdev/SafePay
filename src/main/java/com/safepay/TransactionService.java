package com.safepay;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

/**
 * Service layer: in-memory storage for transactions.
 */
@Service
public class TransactionService {

    private final Map<String, Transaction> transactions = new LinkedHashMap<>();
    private final RiskProperties riskProperties;

    // TODO (students): validation
    // TODO (students): a risk rule
    // TODO (students): importing data/transactions.csv

    public TransactionService(RiskProperties riskProperties) {
        this.riskProperties = riskProperties;
    }

    public Transaction save(Transaction transaction) {
        transactions.put(transaction.getTransactionId(), transaction);
        return transaction;
    }

    public List<Transaction> findAll() {
        return new ArrayList<>(transactions.values());
    }

    public Optional<Transaction> findById(String id) {
        return Optional.ofNullable(transactions.get(id));
    }

    public String assessRisk(Transaction transaction) {
        boolean highAmount = transaction.getAmount()
                .compareTo(riskProperties.getThreshold()) > 0;
        boolean highLocation = riskProperties.getHighRiskLocations().stream()
                .anyMatch(loc -> loc.equalsIgnoreCase(transaction.getLocation()));
        return (highAmount || highLocation) ? "HIGH" : "LOW";
    }
}
