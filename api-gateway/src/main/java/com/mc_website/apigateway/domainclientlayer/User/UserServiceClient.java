package com.mc_website.apigateway.domainclientlayer.User;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mc_website.apigateway.presentation.User.*;
import com.mc_website.apigateway.utils.Utility;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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

    public UserResponseModel[] getAllCustomers() {
            UserResponseModel[] userResponseModels;
            try {
                String url = CUSTOMER_SERVICE_BASE_URL;
                userResponseModels = restTemplate.getForObject(url, UserResponseModel[].class);
            } catch (HttpClientErrorException ex) {
                throw handleHttpClientException(ex);
            }
            return userResponseModels;
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

        public UserResponseModel getCustomerByEmail(String email) {
            UserResponseModel userResponseModel;
            try {
                String url = CUSTOMER_SERVICE_BASE_URL + "/email?email=" + email;
                userResponseModel = restTemplate
                        .getForObject(url, UserResponseModel.class);

            } catch (HttpClientErrorException ex) {
                throw handleHttpClientException(ex);
            }
            return userResponseModel;
        }

        public UserResponseModel getCustomerByEmailAndPassword(String email, String password) {
            UserResponseModel userResponseModel;
            try {
                String url = CUSTOMER_SERVICE_BASE_URL + "/login";
                UserLoginRequestModel userLoginRequestModel = new UserLoginRequestModel(email, password);
                userResponseModel = restTemplate
                        .postForObject(url, userLoginRequestModel, UserResponseModel.class);

            } catch (HttpClientErrorException ex) {
                throw handleHttpClientException(ex);
            }
            return userResponseModel;
        }

        public UserResponseModel addCustomer(UserRequestModel userRequestModel) {
            UserResponseModel userResponseModel;
            try {
                String url = CUSTOMER_SERVICE_BASE_URL;
                userResponseModel = restTemplate
                        .postForObject(url, userRequestModel, UserResponseModel.class);

            } catch (HttpClientErrorException ex) {
                throw handleHttpClientException(ex);
            }
            return userResponseModel;
        }

        public UserResponseModel updateCustomer(String customerId, UserRequestModel userRequestModel) {
            UserResponseModel userResponseModel;
            try {
                String url = CUSTOMER_SERVICE_BASE_URL + "/" + customerId;
                HttpEntity<UserRequestModel> customerRequestModelHttpEntity = new HttpEntity<>(userRequestModel);
                userResponseModel = restTemplate
                        .exchange(url, HttpMethod.PUT, customerRequestModelHttpEntity, UserResponseModel.class).getBody();
            } catch (HttpClientErrorException ex) {
                throw handleHttpClientException(ex);
            }
            return userResponseModel;
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

        UserResetPwdRequestModel userResetPwdRequestModel = UserResetPwdRequestModel.builder().email(request.getParameter("email")).url(Utility.getSiteURL(request)).build();

        String formPage;
        try {
            String url = CUSTOMER_SERVICE_BASE_URL+"/forgot_password";
            formPage = restTemplate
                    .postForObject(url, userResetPwdRequestModel, String.class);

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

        UserResetPwdWithTokenRequestModel userResetPwdWithTokenRequestModel = UserResetPwdWithTokenRequestModel.builder().token(request.getParameter("token")).password(request.getParameter("password")).build();

        String formPage;
        try {
            String url = CUSTOMER_SERVICE_BASE_URL+"/reset_password";
            formPage = restTemplate
                    .postForObject(url, userResetPwdWithTokenRequestModel, String.class);

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
