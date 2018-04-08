package com.jakubeeee.allegrointegrator.integration.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;

@Data
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShopProduct {

    @JsonProperty(value = "product_id")
    String id;

    @JsonProperty(value = "translations")
    Properties properties;

    @JsonProperty(value = "stock")
    StockDetails stockDetails;

    @JsonProperty(value = "special_offer")
    DiscountDetails discountDetails;

    public String getProductName() {
        if (properties.getPolishProperties() == null) return null;
        else return properties.getPolishProperties().getName();
    }

    public int getProductStock() {
        return parseInt(stockDetails.getStock());
    }

    public float getProductPrice() {
        float priceBeforeDiscount = parseFloat(stockDetails.getPrice());
        float discount = discountDetails != null ? parseFloat(discountDetails.getDiscount()) : 0;
        return priceBeforeDiscount - discount;
    }

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    private class StockDetails {
        String stock;
        String price;
    }

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    private class DiscountDetails {
        String discount;
    }

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    private class Properties {
        @JsonProperty(value = "pl_PL")
        PolishProperties polishProperties;

        @Data
        @FieldDefaults(level = AccessLevel.PRIVATE)
        @NoArgsConstructor
        @AllArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        private class PolishProperties {
            @JsonProperty(value = "name")
            String name;
        }
    }
}
