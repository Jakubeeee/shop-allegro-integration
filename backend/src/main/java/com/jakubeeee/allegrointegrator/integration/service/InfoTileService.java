package com.jakubeeee.allegrointegrator.integration.service;

import com.jakubeeee.allegrointegrator.integration.model.InfoTile;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static com.jakubeeee.allegrointegrator.core.utils.DateUtils.format;
import static com.jakubeeee.allegrointegrator.integration.model.InfoTile.*;
import static com.jakubeeee.allegrointegrator.integration.model.InfoTile.Type.*;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class InfoTileService {

    @Autowired
    IntegrationService integrationService;

    @Autowired
    SchedulerService schedulerService;

    @Getter
    Set<InfoTile> infoTileSet = new HashSet<>();

    public void updateInfoTiles() {
        infoTileSet.clear();
        updateInfoTile(integrationService.getLastUpdateDateTime(), LAST_UPDATE);
        updateInfoTile(integrationService.getProductsUpdatedInLastUpdate(), PRODUCTS_UPDATED);
        updateInfoTile(schedulerService.getNextUpdateDateTime(), NEXT_UPDATE);
        updateInfoTile(schedulerService.getUpdateIntervalInMillis() / 60000 + " minutes", UPDATE_INTERVAL);
    }

    private void updateInfoTile(Object information, Type type) {
        String formattedInformation;
        if (information instanceof LocalDateTime)
            formattedInformation = format((LocalDateTime) information);
        else if (information instanceof Integer)
            formattedInformation = String.valueOf(information);
        else
            formattedInformation = (String) information;

        try {
            infoTileSet.add(new InfoTile(formattedInformation, type));
        } catch (NullPointerException ignored) {
            LOG.debug("Problem has occurred while updating info tile of type {}", type);
        }
    }
}
