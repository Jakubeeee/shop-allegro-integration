package com.jakubeeee.allegrointegrator.integration.service;

import com.jakubeeee.allegrointegrator.core.utils.ThreadUtils;
import com.jakubeeee.allegrointegrator.integration.exception.ParameterTypeUpdateNotSupported;
import com.jakubeeee.allegrointegrator.integration.exception.ProductNameInvalidException;
import com.jakubeeee.allegrointegrator.integration.exception.ProductNotFoundInAllegroException;
import com.jakubeeee.allegrointegrator.integration.exception.UnsuccessfulUpdateException;
import com.jakubeeee.allegrointegrator.integration.model.AllegroProduct;
import com.jakubeeee.allegrointegrator.integration.model.ShopProduct;
import com.jakubeeee.allegrointegrator.integration.webservice.AllegroWebServiceClient;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Synchronized;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static java.time.LocalDateTime.now;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class IntegrationService {

    @Autowired
    ShopService shopService;

    @Autowired
    AllegroWebServiceClient allegroClient;

    @Autowired
    ProgressMonitorService progressMonitorService;

    @Autowired
    LoggingService loggingService;

    @Autowired
    InfoTileService infoTileService;

    @Getter
    LocalDateTime lastUpdateDateTime;

    @Getter
    int productsUpdatedInLastUpdate;

    @Synchronized
    public void updateAllProducts() {
        if (progressMonitorService.isUpdateInProgress()) return;
        try {
            productsUpdatedInLastUpdate = 0;
            progressMonitorService.startProgress();

            loggingService.info("Searching for products in the shop...");
            List<ShopProduct> productsFromShop = shopService.getAllProductsFromShop();
            loggingService.info("Found " + productsFromShop.size() + " products in the shop");
            LOG.debug(productsFromShop.toString());
            allegroClient.prepareAllegroWebServiceClient();
            loggingService.info("Searching for products in allegro...");
            List<AllegroProduct> productsFromAllegro = allegroClient.getAllegroProductList();
            loggingService.info("Found " + productsFromAllegro.size() + " products in allegro");

            progressMonitorService.setMaxProgress(productsFromAllegro.size());
            loggingService.info("Searching for products that need to be updated...");
            productsFromShop.forEach((productFromShop) -> {
                try {
                    LOG.info(productFromShop.toString());
                    matchProductFromShopAndAllegro(productFromShop, productsFromAllegro);
                } catch (ProductNameInvalidException e) {
                    loggingService.warn("Product from shop with id \"" + productFromShop.getId() + "\" has" +
                            " invalid name");
                } catch (ProductNotFoundInAllegroException e2) {
                    loggingService.warn("Product \"" + productFromShop.getProductName() + "\" was" +
                            " found in the shop but wasn't found in allegro");
                }
            });
            loggingService.info("Update finished successfully. Products updated: " + productsUpdatedInLastUpdate);
        } catch (ResourceAccessException e) {
            loggingService.error("Connection problem. Try again later");
        } catch (Exception e2) {
            e2.printStackTrace();
            loggingService.error("Unknown error has occurred");
        } finally {
            lastUpdateDateTime = now();
            infoTileService.updateInfoTiles();
            progressMonitorService.resetProgress();
        }
    }

    private void matchProductFromShopAndAllegro(ShopProduct productFromShop, List<AllegroProduct> productsFromAllegro)
            throws ProductNotFoundInAllegroException, ProductNameInvalidException {
        val isProductFoundInAllegro = new AtomicBoolean(false);
        val productFromAllegro = new AtomicReference<AllegroProduct>();

        if (productFromShop.getProductName() == null)
            throw new ProductNameInvalidException("Product name is not defined or invalid");

        productsFromAllegro.stream().filter(
                allegroProduct -> allegroProduct.getTitle().equalsIgnoreCase(productFromShop.getProductName()))
                .forEach(allegroProduct -> {
                    productFromAllegro.set(allegroProduct);
                    isProductFoundInAllegro.set(true);
                });

        if (!isProductFoundInAllegro.get())
            throw new ProductNotFoundInAllegroException("Product not found");
        else {
            boolean isProductUpdated = false;

            if (productFromShop.getProductStock() != productFromAllegro.get().getQuantity()) {
                int previousStock = productFromAllegro.get().getQuantity();
//                try {
//                    updateProductQuantity(productFromAllegro.get(), productFromShop.getProductStock());
//                } catch (ParameterTypeUpdateNotSupported e) {
//                    loggingService.warn("Product \n" + productFromAllegro.get().getTitle() + "\" quantity" +
//                            "hasn't been updated because it's quantity type is not supported");
//                    return;
//                }
                loggingService.update("Product \"" + productFromAllegro.get().getTitle() + "\" quantity" +
                        " has been updated from " + previousStock + " to " + productFromShop.getProductStock());
                isProductUpdated = true;
            } else
                loggingService.debug("Product \"" + productFromAllegro.get().getTitle() + "\" quantity" +
                        " hasn't change (" + productFromAllegro.get().getQuantity() + ")");

            if (productFromShop.getProductPrice() != productFromAllegro.get().getPrice()) {
                float previousPrice = productFromAllegro.get().getPrice();
//                try {
//                    updateProductPrice(productFromAllegro.get(), productFromShop.getProductPrice());
//                } catch (ParameterTypeUpdateNotSupported e) {
//                    loggingService.warn("Product \n" + productFromAllegro.get().getTitle() + "\" price" +
//                            "hasn't been updated because it's price type is not supported");
//                    return;
//                }
                loggingService.update("Product \"" + productFromAllegro.get().getTitle() + "\" price" +
                        " has been updated from " + previousPrice + " to " + productFromShop.getProductPrice());
                isProductUpdated = true;
            } else
                loggingService.debug("Product \"" + productFromAllegro.get().getTitle() + "\" price" +
                        " hasn't change (" + productFromAllegro.get().getPrice() + ")");
            if (isProductUpdated) productsUpdatedInLastUpdate++;
            progressMonitorService.advanceProgress();
        }
    }

    @Transactional
    void updateProductQuantity(AllegroProduct allegroProduct, int newQuantity) throws
            ParameterTypeUpdateNotSupported {
        final int PIECES_QUANTITY_TYPE = 1;
        if (allegroProduct.getPriceType() != PIECES_QUANTITY_TYPE)
            throw new ParameterTypeUpdateNotSupported("Cannot update product with unsupported quantity type");
        try {
            allegroClient.updateQuantity(allegroProduct.getId(), newQuantity);
        } catch (UnsuccessfulUpdateException e) {
            loggingService.error("An error has occurred while updating quantity of \"" + allegroProduct.getTitle() + "\"");
        }
        ThreadUtils.sleep(10);
    }

    @Transactional
    void updateProductPrice(AllegroProduct allegroProduct, float newPrice) throws ParameterTypeUpdateNotSupported {
        final int BUT_NOW_PRICE_TYPE = 1;
        if (allegroProduct.getPriceType() != BUT_NOW_PRICE_TYPE)
            throw new ParameterTypeUpdateNotSupported("Cannot update product with unsupported price type");
        try {
            allegroClient.updatePrice(allegroProduct.getId(), newPrice);
        } catch (UnsuccessfulUpdateException e) {
            loggingService.error("An error has occurred while updating price of \n" + allegroProduct.getTitle() + "\"");
        }
        ThreadUtils.sleep(10);
    }

}
