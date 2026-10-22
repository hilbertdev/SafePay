package com.safepay.infrastructure;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

/**
 * Ring: infrastructure
 * This is the database's view of a transaction - deliberately separate from the domain class.
 */
@Entity
public class TransactionEntity {

    @Id
    private String transactionId;
    private String customerId;
    private BigDecimal amount;
    private String currency;
    private String location;
    private LocalDateTime timestamp;

    protected TransactionEntity() {
    }

    public TransactionEntity(String transactionId,
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
