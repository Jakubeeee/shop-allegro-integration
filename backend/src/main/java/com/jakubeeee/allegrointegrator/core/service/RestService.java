package com.jakubeeee.allegrointegrator.core.service;

import com.jakubeeee.allegrointegrator.integration.service.WebApiAuthenticationService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.cxf.common.util.Base64Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static com.jakubeeee.allegrointegrator.integration.model.AuthenticationToken.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class RestService {

    @Autowired
    RestTemplate template;

    @Autowired
    WebApiAuthenticationService webApiAuthenticationService;

    public String getString(String uri) {
        return template.getForObject(uri, String.class);
    }

    public <T> ResponseEntity<T> getJsonObject(String uri, HttpEntity<?> entity, Class<T> responseType) {
        return template.exchange(
                uri,
                HttpMethod.GET,
                entity,
                responseType);
    }

    public <T> ResponseEntity<T> postJsonObject(String uri, HttpEntity<?> entity, Class<T> responseType) {
        return template.exchange(
                uri,
                HttpMethod.POST,
                entity,
                responseType);
    }

    public <T> ResponseEntity<T> postJsonObject(URI uri, HttpEntity<?> entity, Class<T> responseType) {
        return template.exchange(
                uri,
                HttpMethod.POST,
                entity,
                responseType);
    }

    public <T> ResponseEntity<T> putJsonObject(String uri, HttpEntity<?> entity, Class<T> responseType) {
        return template.exchange(
                uri,
                HttpMethod.PUT,
                entity,
                responseType);
    }

    public HttpHeaders generateHeaderWithAuthToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + webApiAuthenticationService.getToken());
        return headers;
    }

    public HttpHeaders generateHeaderWithUsernameAndPassword(String username, String password) {
        String userAndPass = username + ":" + password;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Basic " + Base64Utility.encode(userAndPass.getBytes()));
        return headers;
    }
}
