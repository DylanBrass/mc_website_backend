package com.mc_website.ordersservice.domainclientlayer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mc_website.ordersservice.presentationlayer.Customer.UserResponseModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Component
public class UserServiceClient {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String CUSTOMER_SERVICE_BASE_URL;

    public UserServiceClient(RestTemplate restTemplate,
                             ObjectMapper objectMapper,
                             @Value("${app.users-service.host}") String customerServiceHost,
                             @Value("${app.users-service.port}") String customerServicePort) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.CUSTOMER_SERVICE_BASE_URL = "http://" + customerServiceHost + ":" + customerServicePort + "/api/v1/customers";
    }

    public UserResponseModel getCustomer(String customerId) {
        UserResponseModel userResponseModel;
        try {
            String url = CUSTOMER_SERVICE_BASE_URL + "/" + customerId;
            userResponseModel = restTemplate
                    .getForObject(url, UserResponseModel.class);
        } catch (HttpClientErrorException ex) {
                throw handleHttpClientException(ex);
        }
        return userResponseModel;
    }

    private RuntimeException handleHttpClientException(HttpClientErrorException ex) {
        if (ex.getStatusCode() == NOT_FOUND) {
            //return new NotFoundException(ex.getMessage());
        }
        if (ex.getStatusCode() == UNPROCESSABLE_ENTITY) {
            //return new InvalidInputException(ex.getMessage());
        }
        return ex;
    }
    }
