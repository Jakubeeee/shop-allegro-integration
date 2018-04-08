package com.jakubeeee.allegrointegrator.integration.controller;

import com.jakubeeee.allegrointegrator.integration.model.InfoTile;
import com.jakubeeee.allegrointegrator.integration.service.InfoTileService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RestController
public class InfoTileController {

    @Autowired
    InfoTileService infoTileService;

    @GetMapping("/updateInfoTileData")
    public Set<InfoTile> updateInfoTileData() {
        return infoTileService.getInfoTileSet();
    }
}
