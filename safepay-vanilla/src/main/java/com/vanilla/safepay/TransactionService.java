package com.vanilla.safepay;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Application layer: in-memory storage for transactions. */
public class TransactionService {

    private final Map<String, Transaction> transactions = new LinkedHashMap<>();

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
}
