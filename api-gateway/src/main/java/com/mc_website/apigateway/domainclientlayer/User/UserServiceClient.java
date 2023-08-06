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
    private final String USER_SERVICE_BASE_URL;

    public UserServiceClient(RestTemplate restTemplate,
                             ObjectMapper objectMapper,
                             @Value("${app.users-service.host}") String userServiceHost,
                             @Value("${app.users-service.port}") String userServicePort) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.USER_SERVICE_BASE_URL = "http://" + userServiceHost + ":" + userServicePort + "/api/v1/users";
    }

    public UserResponseModel[] getAllUsers() {
            UserResponseModel[] userResponseModels;
            try {
                String url = USER_SERVICE_BASE_URL;
                userResponseModels = restTemplate.getForObject(url, UserResponseModel[].class);
            } catch (HttpClientErrorException ex) {
                throw handleHttpClientException(ex);
            }
            return userResponseModels;
        }

        public UserResponseModel getUser(String userId) {
            UserResponseModel userResponseModel;
            try {
                String url = USER_SERVICE_BASE_URL + "/" + userId;
                userResponseModel = restTemplate
                        .getForObject(url, UserResponseModel.class);

            } catch (HttpClientErrorException ex) {
                throw handleHttpClientException(ex);
            }
            return userResponseModel;
        }

        public UserResponseModel getUserByEmail(String email) {
            UserResponseModel userResponseModel;
            try {
                String url = USER_SERVICE_BASE_URL + "/email?email=" + email;
                userResponseModel = restTemplate
                        .getForObject(url, UserResponseModel.class);

            } catch (HttpClientErrorException ex) {
                throw handleHttpClientException(ex);
            }
            return userResponseModel;
        }

        public UserResponseModel getUserByEmailAndPassword(String email, String password) {
            UserResponseModel userResponseModel;
            try {
                String url = USER_SERVICE_BASE_URL + "/login";
                UserLoginRequestModel userLoginRequestModel = new UserLoginRequestModel(email, password);
                userResponseModel = restTemplate
                        .postForObject(url, userLoginRequestModel, UserResponseModel.class);

            } catch (HttpClientErrorException ex) {
                throw handleHttpClientException(ex);
            }
            return userResponseModel;
        }

        public UserResponseModel addUser(UserRequestModel userRequestModel) {
            UserResponseModel userResponseModel;
            try {
                String url = USER_SERVICE_BASE_URL;
                userResponseModel = restTemplate
                        .postForObject(url, userRequestModel, UserResponseModel.class);

            } catch (HttpClientErrorException ex) {
                throw handleHttpClientException(ex);
            }
            return userResponseModel;
        }

        public UserResponseModel updateUser(String userId, UserRequestModel userRequestModel) {
            UserResponseModel userResponseModel;
            try {
                String url = USER_SERVICE_BASE_URL + "/" + userId;
                HttpEntity<UserRequestModel> userRequestModelHttpEntity = new HttpEntity<>(userRequestModel);
                userResponseModel = restTemplate
                        .exchange(url, HttpMethod.PUT, userRequestModelHttpEntity, UserResponseModel.class).getBody();
            } catch (HttpClientErrorException ex) {
                throw handleHttpClientException(ex);
            }
            return userResponseModel;
        }

        public void deleteUser(String userId) {
            try {
                String url = USER_SERVICE_BASE_URL + "/" + userId;
                restTemplate
                        .delete(url);

            } catch (HttpClientErrorException ex) {
                throw handleHttpClientException(ex);
            }
        }

    public String userForgotPassword() {
        String form;
        try {
            String url = USER_SERVICE_BASE_URL + "/forgot_password";
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
            String url = USER_SERVICE_BASE_URL+"/forgot_password";
            formPage = restTemplate
                    .postForObject(url, userResetPwdRequestModel, String.class);

        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
        return formPage;
    }
    public String userShowResetPage(String token) {
        String form;
        try {
            String url = USER_SERVICE_BASE_URL + "/reset_password";
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
            String url = USER_SERVICE_BASE_URL+"/reset_password";
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
