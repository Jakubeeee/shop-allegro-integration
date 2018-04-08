package com.jakubeeee.allegrointegrator.integration.controller;

import com.jakubeeee.allegrointegrator.integration.model.ProgressMonitor;
import com.jakubeeee.allegrointegrator.integration.service.ProgressMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProgressController {

    @Autowired
    ProgressMonitorService progressMonitorService;

    @GetMapping("/progress")
    public ProgressMonitor getProgress() {
        return progressMonitorService.getProgressMonitor();
    }
}
