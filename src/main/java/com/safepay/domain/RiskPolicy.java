package com.safepay.domain;

import java.math.BigDecimal;
import java.util.List;

/**
 * Ring: domain
 * This is a business rule, so it lives in the domain.
 */
public class RiskPolicy {

    private final BigDecimal threshold;
    private final List<String> highRiskLocations;

    public RiskPolicy(BigDecimal threshold, List<String> highRiskLocations) {
        this.threshold = threshold;
        this.highRiskLocations = highRiskLocations;
    }

    public String assess(Transaction transaction) {
        boolean highAmount = transaction.getAmount()
                .compareTo(threshold) > 0;
        boolean highLocation = highRiskLocations.stream()
                .anyMatch(loc -> loc.equalsIgnoreCase(transaction.getLocation()));
        return (highAmount || highLocation) ? "HIGH" : "LOW";
    }
}
