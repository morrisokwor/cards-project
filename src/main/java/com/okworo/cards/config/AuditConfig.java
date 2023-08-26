package com.okworo.cards.config;

import com.okworo.cards.services.AuditorAwareImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @author Morris.Okworo on 26/08/2023
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AuditConfig {
    @Bean
    public AuditorAware<Long> auditorProvider() {
        // our implementation of AuditorAware
        return new AuditorAwareImpl();
    }

}
