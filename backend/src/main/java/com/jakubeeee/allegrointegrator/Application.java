package com.jakubeeee.allegrointegrator;

import com.jakubeeee.allegrointegrator.integration.service.SchedulerService;
import com.jakubeeee.allegrointegrator.security.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Value("${applicationUsername}")
    String APPLICATION_USERNAME;

    @Value("${applicationPassword}")
    String APPLICATION_PASSWORD;

    @Value("${enableScheduledUpdates}")
    boolean enableScheduledUpdates;

    @Bean
    public CommandLineRunner launchStartupTasks(UserService userService, SchedulerService schedulerService) {
        return (args) -> {
            userService.createUser(APPLICATION_USERNAME, APPLICATION_PASSWORD, "none");
            if (enableScheduledUpdates) schedulerService.scheduleUpdates();
        };
    }
}