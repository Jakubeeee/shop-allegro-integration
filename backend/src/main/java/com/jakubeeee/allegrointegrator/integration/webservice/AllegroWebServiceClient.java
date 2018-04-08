package com.jakubeeee.allegrointegrator.integration.webservice;

import com.jakubeeee.allegrointegrator.integration.exception.UnsuccessfulUpdateException;
import com.jakubeeee.allegrointegrator.integration.executor.UpdateExecutor;
import com.jakubeeee.allegrointegrator.integration.model.AllegroProduct;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import pl.allegro.webapi.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Component
public class AllegroWebServiceClient extends WebServiceGatewaySupport {

    @Value("${allegroUsername}")
    String ALLEGRO_USERNAME;

    @Value("${allegroPassword}")
    String ALLEGRO_PASSWORD;

    @Value("${allegroWebApiKey}")
    String ALLEGRO_WEB_API_KEY;

    final int ALLEGRO_COUNTRY_CODE = 1;
    long allegroLocalVersion = -1;

    ServicePort allegro;
    String sessionHandlePart;
    long userId;
    long serverTime;

    public void prepareAllegroWebServiceClient() {
        allegro = new ServiceService().getServicePort();
        handleSystemStatusTypes();
        loginToAllegro();
    }

    private void handleSystemStatusTypes() {
        val request = new DoQueryAllSysStatusRequest(ALLEGRO_COUNTRY_CODE, ALLEGRO_WEB_API_KEY);
        val response = allegro.doQueryAllSysStatus(request);
        ArrayOfSysstatustype sysStatusTypeArray = response.getSysCountryStatus();
        for (SysStatusType type : sysStatusTypeArray.getItem())
            if (type.getCountryId() == ALLEGRO_COUNTRY_CODE)
                allegroLocalVersion = type.getVerKey();
    }

    private void loginToAllegro() {
        DoLoginRequest request = new DoLoginRequest(
                ALLEGRO_USERNAME,
                ALLEGRO_PASSWORD,
                ALLEGRO_COUNTRY_CODE,
                ALLEGRO_WEB_API_KEY,
                allegroLocalVersion);
        DoLoginResponse response = allegro.doLogin(request);
        sessionHandlePart = response.getSessionHandlePart();
        userId = response.getUserId();
        serverTime = response.getServerTime();
        LOG.debug("Allegro user id: {}", userId);
        LOG.debug("Allegro session id: {}", sessionHandlePart);
    }

    public List<AllegroProduct> getAllegroProductList() {
        int pageNumber = 0;
        int productsInAllegroCounter;

        DoGetMySellItemsResponse response = getAllegroProductListFromPage(pageNumber);
        List<SellItemStruct> sellItemStructList = new ArrayList<>(response.getSellItemsList().getItem());
        productsInAllegroCounter = response.getSellItemsCounter();
        int productsLeftToFetch = productsInAllegroCounter - response.getSellItemsList().getItem().size();
        LOG.debug("productsLeftToFetch: " + productsLeftToFetch);

        while (productsLeftToFetch > 0) {
            LOG.debug("productsLeftToFetch: " + productsLeftToFetch);
            response = getAllegroProductListFromPage(pageNumber);
            sellItemStructList.addAll(response.getSellItemsList().getItem());
            LOG.debug("list size: " + String.valueOf(response.getSellItemsList().getItem().size()));
            productsLeftToFetch -= response.getSellItemsList().getItem().size();
        }

        return convertToAllegroProducts(sellItemStructList);
    }

    private DoGetMySellItemsResponse getAllegroProductListFromPage(int pageNumber) {
        val request = new DoGetMySellItemsRequest(
                sessionHandlePart,
                null,
                null,
                null,
                null,
                null,
                1000,
                pageNumber);
        return allegro.doGetMySellItems(request);
    }

    private List<AllegroProduct> convertToAllegroProducts(List<SellItemStruct> sellItemStructList) {
        List<AllegroProduct> allegroProductList = new ArrayList<>();
        sellItemStructList.forEach((item) -> {
            List<ItemPriceStruct> itemPriceStructList = item.getItemPrice().getItem();
            ItemPriceStruct itemPriceStruct;
            if (itemPriceStructList.size() != 1 || (itemPriceStruct = itemPriceStructList.get(0)).getPriceType() != 1) {
                throw new IllegalArgumentException("Cannot support auction with sale different than buy now");
            }
            allegroProductList.add(convertToAllegroProduct(item, itemPriceStruct));
        });
        LOG.debug("Fetched product list from allegro: " + allegroProductList.toString());
        return allegroProductList;
    }

    private AllegroProduct convertToAllegroProduct(SellItemStruct sellItemStruct, ItemPriceStruct itemPriceStruct) {
        return AllegroProduct.builder()
                .id(sellItemStruct.getItemId())
                .title(sellItemStruct.getItemTitle())
                .quantity(sellItemStruct.getItemStartQuantity())
                .quantityType(sellItemStruct.getItemQuantityType())
                .price(itemPriceStruct.getPriceValue())
                .priceType(itemPriceStruct.getPriceType())
                .build();
    }

    public void updateQuantity(long productId, int newQuantity) throws UnsuccessfulUpdateException {
        val request = new DoChangeQuantityItemRequest(sessionHandlePart, productId, newQuantity);
        UpdateExecutor.execute(() -> allegro.doChangeQuantityItem(request));
    }

    public void updatePrice(long productId, float newPrice) throws UnsuccessfulUpdateException {
        DoChangePriceItemRequest request = new DoChangePriceItemRequest(
                sessionHandlePart,
                productId,
                null,
                null,
                newPrice,
                null);
        UpdateExecutor.execute(() -> allegro.doChangePriceItem(request));
    }

}

