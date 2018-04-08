package com.jakubeeee.allegrointegrator.integration.service;

import com.jakubeeee.allegrointegrator.core.service.RestService;
import com.jakubeeee.allegrointegrator.integration.model.ProductsCollection;
import com.jakubeeee.allegrointegrator.integration.model.ShopProduct;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class ShopService {

    @Autowired
    ProgressMonitorService progressMonitorService;

    @Autowired
    RestService restService;

    @Value("${shopProductsUri}")
    String SHOP_PRODUCTS_URI;

    public List<ShopProduct> getAllProductsFromShop() {
        List<ShopProduct> productList = new ArrayList<>();
        int pageAmount = getPageAmount();

        for (int i = 1; i <= pageAmount; i++)
            productList.addAll(getAllProductsFromPage(i));

        return productList;
    }

    private int getPageAmount() {
        HttpHeaders headers = restService.generateHeaderWithAuthToken();
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<ProductsCollection> response = restService.getJsonObject(
                SHOP_PRODUCTS_URI + "?limit=50", entity, ProductsCollection.class);

        return response.getBody().getPages();
    }

    private List<ShopProduct> getAllProductsFromPage(int page) {
        HttpHeaders headers = restService.generateHeaderWithAuthToken();
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<ProductsCollection> response = restService.getJsonObject(
                SHOP_PRODUCTS_URI + "?limit=50&page=" + page,
                entity, ProductsCollection.class);

        return response.getBody().getList();
    }

}
