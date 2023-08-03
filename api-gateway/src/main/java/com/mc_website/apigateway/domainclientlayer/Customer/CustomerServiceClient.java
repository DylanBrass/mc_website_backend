package com.mc_website.apigateway.domainclientlayer.Customer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mc_website.apigateway.presentation.Customer.*;
import com.mc_website.apigateway.utils.Utility;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

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

        public CustomerResponseModel getCustomerByEmail(String email) {
            CustomerResponseModel customerResponseModel;
            try {
                String url = CUSTOMER_SERVICE_BASE_URL + "/email?email=" + email;
                customerResponseModel = restTemplate
                        .getForObject(url, CustomerResponseModel.class);

            } catch (HttpClientErrorException ex) {
                throw handleHttpClientException(ex);
            }
            return customerResponseModel;
        }

        public CustomerResponseModel getCustomerByEmailAndPassword(String email, String password) {
            CustomerResponseModel customerResponseModel;
            try {
                String url = CUSTOMER_SERVICE_BASE_URL + "/login";
                CustomerLoginRequestModel customerLoginRequestModel = new CustomerLoginRequestModel(email, password);
                customerResponseModel = restTemplate
                        .postForObject(url, customerLoginRequestModel, CustomerResponseModel.class);

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
                HttpEntity<CustomerRequestModel> customerRequestModelHttpEntity = new HttpEntity<>(customerRequestModel);
                customerResponseModel = restTemplate
                        .exchange(url, HttpMethod.PUT, customerRequestModelHttpEntity, CustomerResponseModel.class).getBody();
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

    public String customerForgotPassword() {
        String form;
        try {
            String url = CUSTOMER_SERVICE_BASE_URL + "/forgot_password";
            form = restTemplate
                    .getForObject(url, String.class);

        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
        return form;
    }

    public String sendForgottenEmail(HttpServletRequest request) {

        CustomerResetPwdRequestModel customerResetPwdRequestModel = CustomerResetPwdRequestModel.builder().email(request.getParameter("email")).url(Utility.getSiteURL(request)).build();

        String formPage;
        try {
            String url = CUSTOMER_SERVICE_BASE_URL+"/forgot_password";
            formPage = restTemplate
                    .postForObject(url, customerResetPwdRequestModel, String.class);

        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
        return formPage;
    }
    public String customerShowResetPage(String token) {
        String form;
        try {
            String url = CUSTOMER_SERVICE_BASE_URL + "/reset_password";
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
            builder.queryParam("token",token);
            url = builder.toUriString();

            form = restTemplate
                    .getForObject(url, String.class);

        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
        return form;
    }
    public String changePassword(HttpServletRequest request) {

        CustomerResetPwdWithTokenRequestModel customerResetPwdWithTokenRequestModel = CustomerResetPwdWithTokenRequestModel.builder().token(request.getParameter("token")).password(request.getParameter("password")).build();

        String formPage;
        try {
            String url = CUSTOMER_SERVICE_BASE_URL+"/reset_password";
            formPage = restTemplate
                    .postForObject(url, customerResetPwdWithTokenRequestModel, String.class);

        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
        return formPage;
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
