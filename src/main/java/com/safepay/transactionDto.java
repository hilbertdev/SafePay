package com.safepay;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionDto(
        String transactionId,
        String customerId,
        BigDecimal amount,
        String currency,
        String location,
        LocalDateTime timestamp
) {
}
