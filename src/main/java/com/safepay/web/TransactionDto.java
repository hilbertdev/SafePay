package com.safepay.web;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Ring: web adapter
 */
public record TransactionDto(
        String transactionId,
        String customerId,
        BigDecimal amount,
        String currency,
        String location,
        LocalDateTime timestamp
) {
}
