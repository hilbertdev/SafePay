package com.safepay;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Spring Boot binds the safepay.risk section from application.yml to this class at startup
 * (like {@code IOptions<T>} in .NET).
 */
@Component
@ConfigurationProperties(prefix = "safepay.risk")
public class RiskProperties {

    private BigDecimal threshold;
    private List<String> highRiskLocations;

    public BigDecimal getThreshold() {
        return threshold;
    }

    public void setThreshold(BigDecimal threshold) {
        this.threshold = threshold;
    }

    public List<String> getHighRiskLocations() {
        return highRiskLocations;
    }

    public void setHighRiskLocations(List<String> highRiskLocations) {
        this.highRiskLocations = highRiskLocations;
    }
}
