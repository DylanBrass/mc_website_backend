package com.mc_website.apigateway.domainclientlayer.Orders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mc_website.apigateway.presentation.Orders.OrderRequestModel;
import com.mc_website.apigateway.presentation.Orders.OrderResponseModel;
import com.mc_website.apigateway.utils.InvalidInputException;
import com.mc_website.apigateway.utils.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
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
        this.ORDERS_SERVICE_BASE_URL = "http://" + orderServiceHost + ":" + orderServicePort + "/api/v1/users";
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


    public OrderResponseModel[] getAllUsersOrders(String userId) {
        OrderResponseModel[] orderResponseModels;
        try {
            String url = ORDERS_SERVICE_BASE_URL + "/" + userId + "/orders";
            orderResponseModels = restTemplate.getForObject(url, OrderResponseModel[].class);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
        return orderResponseModels;
    }

    public OrderResponseModel addOrder(OrderRequestModel orderRequestModel,String userId) {
        OrderResponseModel orderResponseModel;
        try {
            String url = ORDERS_SERVICE_BASE_URL + "/"+ userId + "/orders";

            orderResponseModel = restTemplate
                    .postForObject(url, orderRequestModel, OrderResponseModel.class);

        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
        return orderResponseModel;
    }

    public OrderResponseModel updateOrder(OrderRequestModel orderRequestModel,String orderId) {
        OrderResponseModel orderResponseModel;
        try {
            String url = ORDERS_SERVICE_BASE_URL + "/orders/"+orderId;

            HttpEntity<OrderRequestModel> orderRequestModelHttpEntity = new HttpEntity<>(orderRequestModel);
            orderResponseModel = restTemplate
                    .exchange(url, HttpMethod.PUT,orderRequestModelHttpEntity, OrderResponseModel.class).getBody();
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
        return orderResponseModel;
    }
    private RuntimeException handleHttpClientException(HttpClientErrorException ex) {
        if (ex.getStatusCode() == NOT_FOUND) {
            return new NotFoundException(ex.getMessage());
        }
        if (ex.getStatusCode() == UNPROCESSABLE_ENTITY) {
            return new InvalidInputException(ex.getMessage());
        }
        return ex;
    }
}
