package com.jakubeeee.allegrointegrator.integration.controller;

import com.jakubeeee.allegrointegrator.integration.service.IntegrationService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RestController
public class IntegrationController {

    @Autowired
    IntegrationService integrationService;

    @PostMapping("/updateProducts")
    public void updateProducts() {
        integrationService.updateAllProducts();
    }

}
