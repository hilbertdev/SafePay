package com.vanilla.safepay;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Domain model: one payment transaction. */
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
}
