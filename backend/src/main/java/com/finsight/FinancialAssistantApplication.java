package com.finsight;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Main application class for FinSight AI - Personal Financial Education Platform
 * 
 * This application provides:
 * - User authentication and profile management
 * - Goal-based financial education
 * - Adaptive quiz system with dynamic question bank
 * - Bank integration for personalized recommendations
 * - Progress tracking and analytics
 */
@SpringBootApplication
@EnableCaching
@EnableAsync
public class FinancialAssistantApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinancialAssistantApplication.class, args);
    }
}
