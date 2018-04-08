package com.jakubeeee.allegrointegrator.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@PropertySource("classpath:app-config.properties")
@EnableTransactionManagement
public class ApplicationConfig {

}
