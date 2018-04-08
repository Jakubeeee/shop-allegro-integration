package com.jakubeeee.allegrointegrator.integration.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class InfoTile {

    public enum Type {
        LAST_UPDATE,
        PRODUCTS_UPDATED,
        NEXT_UPDATE,
        UPDATE_INTERVAL
    }
    
    String information;
    Type type;
}
