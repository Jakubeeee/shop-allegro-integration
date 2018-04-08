package com.jakubeeee.allegrointegrator.integration.controller;

import com.jakubeeee.allegrointegrator.integration.model.LogMessage;
import com.jakubeeee.allegrointegrator.integration.service.LoggingService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.jakubeeee.allegrointegrator.integration.model.LogMessage.*;
import static com.jakubeeee.allegrointegrator.integration.model.LogMessage.Type.*;
import static java.time.LocalDateTime.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RestController
public class LoggingController {

    @Autowired
    LoggingService loggingService;

    @GetMapping("/logs")
    public List<LogMessage> getLogs() {
        loggingService.removeOldLogs();
        return loggingService.getLogList();
    }

    @PostMapping("/clearLogs")
    public void clearLogs() {
        loggingService.clearLogList();
    }

}
