package com.mc_website.apigateway.domainclientlayer.Orders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mc_website.apigateway.presentation.Customer.CustomerRequestModel;
import com.mc_website.apigateway.presentation.Customer.CustomerResponseModel;
import com.mc_website.apigateway.presentation.Orders.OrderRequestModel;
import com.mc_website.apigateway.presentation.Orders.OrderResponseModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Component
public class OrderServiceClient {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String ORDERS_SERVICE_BASE_URL;


    public OrderServiceClient(RestTemplate restTemplate, ObjectMapper objectMapper,@Value("${app.orders-service.host}") String orderServiceHost,
                              @Value("${app.orders-service.port}") String orderServicePort) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.ORDERS_SERVICE_BASE_URL = "http://" + orderServiceHost + ":" + orderServicePort + "/api/v1/customers";
    }
    public OrderResponseModel[] getAllOrders() {
        OrderResponseModel[] orderResponseModels;
        try {
            String url = ORDERS_SERVICE_BASE_URL + "/orders";
            orderResponseModels = restTemplate.getForObject(url, OrderResponseModel[].class);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
        return orderResponseModels;
    }


    public OrderResponseModel[] getAllCustomersOrders(String customerId) {
        OrderResponseModel[] orderResponseModels;
        try {
            String url = ORDERS_SERVICE_BASE_URL + "/" + customerId + "/orders";
            orderResponseModels = restTemplate.getForObject(url, OrderResponseModel[].class);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
        return orderResponseModels;
    }

    public OrderResponseModel addOrder(OrderRequestModel orderRequestModel,String customerId) {
        OrderResponseModel orderResponseModel;
        try {
            String url = ORDERS_SERVICE_BASE_URL + "/"+ customerId + "/orders";

            orderResponseModel = restTemplate
                    .postForObject(url, orderRequestModel, OrderResponseModel.class);

        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
        return orderResponseModel;
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
