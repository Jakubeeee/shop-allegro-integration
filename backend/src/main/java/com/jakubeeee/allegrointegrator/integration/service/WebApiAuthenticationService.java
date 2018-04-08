package com.jakubeeee.allegrointegrator.integration.service;

import com.jakubeeee.allegrointegrator.core.service.RestService;
import com.jakubeeee.allegrointegrator.integration.model.AuthenticationToken;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class WebApiAuthenticationService {

    @Value("${shopAuthUri}")
    String WEB_API_AUTH_URI;
    @Value("${shopAdminUsername}")
    String USERNAME;
    @Value("${shopAdminPassword}")
    String PASSWORD;

    String token;

    @Autowired
    private RestService restService;

    public String getToken() {
        return token != null ? token : getNewToken();
    }

    private String getNewToken() {
        HttpHeaders headers = restService.generateHeaderWithUsernameAndPassword(USERNAME, PASSWORD);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<AuthenticationToken> response =
                restService.postJsonObject(WEB_API_AUTH_URI, entity, AuthenticationToken.class);
        token = response.getBody().getValue();
        return token;
    }

}
