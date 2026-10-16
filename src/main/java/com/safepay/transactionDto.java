package com.safepay;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class transactionDto {
     String transactionId;
     String customerId;
     BigDecimal amount;
     String currency;
     String location;
     LocalDateTime timestamp;
     String transactionMetadata;

    public transactionDto() {
    }
}
