package com.safepay.infrastructure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.safepay.domain.RiskPolicy;

/**
 * Ring: infrastructure
 * Configuration is infrastructure - it enters the onion as plain values, so the application ring never imports an infrastructure type.
 */
@Configuration
public class RiskConfig {

    @Bean
    public RiskPolicy riskPolicy(RiskProperties riskProperties) {
        return new RiskPolicy(riskProperties.getThreshold(), riskProperties.getHighRiskLocations());
    }
}
