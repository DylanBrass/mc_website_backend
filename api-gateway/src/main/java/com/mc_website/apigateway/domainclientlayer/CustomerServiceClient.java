package com.mc_website.apigateway.domainclientlayer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mc_website.apigateway.presentation.CustomerRequestModel;
import com.mc_website.apigateway.presentation.CustomerResponseModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Component
public class CustomerServiceClient {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String CUSTOMER_SERVICE_BASE_URL;

    public CustomerServiceClient(RestTemplate restTemplate,
                                ObjectMapper objectMapper,
                                @Value("${app.customers-service.host}") String customerServiceHost,
                                @Value("${app.customers-service.port}") String customerServicePort) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.CUSTOMER_SERVICE_BASE_URL = "http://" + customerServiceHost + ":" + customerServicePort + "/api/v1/customers";
    }

    public CustomerResponseModel[] getAllCustomers() {
            CustomerResponseModel[] customerResponseModels;
            try {
                String url = CUSTOMER_SERVICE_BASE_URL;
                customerResponseModels = restTemplate.getForObject(url, CustomerResponseModel[].class);
            } catch (HttpClientErrorException ex) {
                throw handleHttpClientException(ex);
            }
            return customerResponseModels;
        }

        public CustomerResponseModel getCustomer(String customerId) {
            CustomerResponseModel customerResponseModel;
            try {
                String url = CUSTOMER_SERVICE_BASE_URL + "/" + customerId;
                customerResponseModel = restTemplate
                        .getForObject(url, CustomerResponseModel.class);

            } catch (HttpClientErrorException ex) {
                throw handleHttpClientException(ex);
            }
            return customerResponseModel;
        }

        public CustomerResponseModel addCustomer(CustomerRequestModel customerRequestModel) {
            CustomerResponseModel customerResponseModel;
            try {
                String url = CUSTOMER_SERVICE_BASE_URL;
                customerResponseModel = restTemplate
                        .postForObject(url, customerRequestModel, CustomerResponseModel.class);

            } catch (HttpClientErrorException ex) {
                throw handleHttpClientException(ex);
            }
            return customerResponseModel;
        }

        public CustomerResponseModel updateCustomer(String customerId, CustomerRequestModel customerRequestModel) {
            CustomerResponseModel customerResponseModel;
            try {
                String url = CUSTOMER_SERVICE_BASE_URL + "/" + customerId;
                restTemplate
                        .put(url, customerRequestModel);
                customerResponseModel = getCustomer(customerId);

            } catch (HttpClientErrorException ex) {
                throw handleHttpClientException(ex);
            }
            return customerResponseModel;
        }

        public void deleteCustomer(String customerId) {
            try {
                String url = CUSTOMER_SERVICE_BASE_URL + "/" + customerId;
                restTemplate
                        .delete(url);

            } catch (HttpClientErrorException ex) {
                throw handleHttpClientException(ex);
            }
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
