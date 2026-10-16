package com.safepay;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * One payment that was read from the CSV file and converted into Java types.
 */
public class Transaction {

    private final String transactionId;
    private final String customerId;
    private final BigDecimal amount;
    private final String currency;
    private final String location;
    private final LocalDateTime timestamp;

    public Transaction(String transactionId,
                       String customerId,
                       BigDecimal amount,
                       String currency,
                       String location,
                       LocalDateTime timestamp) {
        this.transactionId = transactionId;
        this.customerId = customerId;
        this.amount = amount;
        this.currency = currency;
        this.location = location;
        this.timestamp = timestamp;
    }

    public Transaction(transactionDto transaction) {
        this.transactionId = transaction.transactionId;
        this.customerId = transaction.customerId;
        this.amount = transaction.amount;
        this.currency = transaction.currency;
        this.location = transaction.location;
        this.timestamp = transaction.timestamp;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getLocation() {
        return location;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Transaction{"
                + "transactionId='" + transactionId + "'"
                + ", customerId='" + customerId + "'"
                + ", amount=" + amount
                + ", currency='" + currency + "'"
                + ", location='" + location + "'"
                + ", timestamp=" + timestamp
                + "}";
    }
}
