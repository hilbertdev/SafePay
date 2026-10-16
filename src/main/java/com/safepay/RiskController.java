package com.safepay;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Accepts one transaction submitted as JSON and returns that same object.
 * This lesson does not store the payment or assign a risk score.
 */
@RestController
public class RiskController {

    @PostMapping("/transactions")
    public Transaction submit(@RequestBody transactionDto transaction) {

        var transactionEntity = new Transaction(transaction);
        return transactionEntity;
    }
}
