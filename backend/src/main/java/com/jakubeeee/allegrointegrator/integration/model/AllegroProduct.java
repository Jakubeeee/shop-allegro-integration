package com.jakubeeee.allegrointegrator.integration.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AllegroProduct {
    long id;
    String title;
    int quantity;
    int quantityType;
    float price;
    int priceType;
}

