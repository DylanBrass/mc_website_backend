package com.mc_website.apigateway.domainclientlayer.User;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mc_website.apigateway.presentation.User.UserLoginRequestModel;
import com.mc_website.apigateway.presentation.User.UserRequestModel;
import com.mc_website.apigateway.presentation.User.UserResponseModel;
import com.mc_website.apigateway.utils.exceptions.ExistingUserNotFoundException;
import com.mc_website.apigateway.utils.exceptions.InvalidInputException;
import com.mc_website.apigateway.utils.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.*;

class UserServiceClientUnitTest {

    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;

    private String USER_SERVICE_BASE_URL;
    private UserServiceClient userServiceClient;

    @BeforeEach
    public void setup(){
        restTemplate = Mockito.mock(RestTemplate.class);
        userServiceClient = new UserServiceClient(restTemplate, objectMapper, "localhost", "8080");
        this.objectMapper = new ObjectMapper();
        this.USER_SERVICE_BASE_URL = "http://localhost:8080/api/v1/users";
    }

    @Test
    void getAllUsersReturnsArrayOfUsers() {
        UserResponseModel[] expectedUserResponseModels = new UserResponseModel[2];
        expectedUserResponseModels[0] = UserResponseModel.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phoneNumber("5147890876")
                .password("passingAWord")
                .build();
        expectedUserResponseModels[1] = UserResponseModel.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.come")
                .phoneNumber("5147890877")
                .password("passingWorDDS!")
                .build();

        when(restTemplate.getForObject(USER_SERVICE_BASE_URL, UserResponseModel[].class))
                .thenReturn(expectedUserResponseModels);

        UserResponseModel[] actualUserResponseModels = userServiceClient.getAllUsers();
        assertArrayEquals(expectedUserResponseModels, actualUserResponseModels);
    }

    @Test
    void getAllUsers_whenRestTemplateThrowsHttpClientErrorException_shouldThrowNotFoundException(){
        HttpClientErrorException exception = new HttpClientErrorException(NOT_FOUND, "Not found");

        when(restTemplate.getForObject(USER_SERVICE_BASE_URL, UserResponseModel[].class))
                .thenThrow(exception);

        assertThrows(NotFoundException.class, () -> userServiceClient.getAllUsers());
    }

    @Test
    void getAllUsers_whenRestTemplateThrowsHttpClientErrorException_shouldThrowUnprocessableEntityException(){
        HttpClientErrorException exception = new HttpClientErrorException(UNPROCESSABLE_ENTITY, "Unprocessable Entity");

        when(restTemplate.getForObject(USER_SERVICE_BASE_URL, UserResponseModel[].class))
                .thenThrow(exception);

        assertThrows(InvalidInputException.class, () -> userServiceClient.getAllUsers());
    }

    @Test
    void getAllUsers_whenRestTemplateThrowsHttpClientErrorException_shouldThrowExistingUserNotFoundException(){
        HttpClientErrorException exception = new HttpClientErrorException(BAD_REQUEST, "Existing User Not Found");

        when(restTemplate.getForObject(USER_SERVICE_BASE_URL, UserResponseModel[].class))
                .thenThrow(exception);

        assertThrows(ExistingUserNotFoundException.class, () -> userServiceClient.getAllUsers());
    }

    @Test
    void getUserReturnsUser() {
        String userId = "1234";

        UserResponseModel expectedUserResponseModel = UserResponseModel.builder()
        .firstName("Johnny")
        .lastName("Doeyy")
        .email("johny.doey@example.com")
        .phoneNumber("5147899876")
        .password("pasAWord")
        .build();

        when(restTemplate.getForObject(USER_SERVICE_BASE_URL + "/" + userId, UserResponseModel.class))
        .thenReturn(expectedUserResponseModel);

        UserResponseModel actualUserResponseModel = userServiceClient.getUser(userId);
        assertEquals(expectedUserResponseModel, actualUserResponseModel);

    }

    @Test
    void getUser_whenRestTemplateThrowsHttpClientErrorException_shouldThrowNotFoundException(){
        String userId = "1234";
        HttpClientErrorException exception = new HttpClientErrorException(NOT_FOUND, "Not found");

        when(restTemplate.getForObject(USER_SERVICE_BASE_URL + "/" + userId, UserResponseModel.class))
                .thenThrow(exception);

        assertThrows(NotFoundException.class, () -> userServiceClient.getUser(userId));
    }

    @Test
    void getUser_whenRestTemplateThrowsHttpClientErrorException_shouldThrowUnprocessableEntityException(){
        String userId = "1234";
        HttpClientErrorException exception = new HttpClientErrorException(UNPROCESSABLE_ENTITY, "Unprocessable Entity");

        when(restTemplate.getForObject(USER_SERVICE_BASE_URL + "/" + userId, UserResponseModel.class))
                .thenThrow(exception);

        assertThrows(InvalidInputException.class, () -> userServiceClient.getUser(userId));
    }

    @Test
    void getUser_whenRestTemplateThrowsHttpClientErrorException_shouldThrowExistingUserNotFoundException(){
        String userId = "1234";
        HttpClientErrorException exception = new HttpClientErrorException(BAD_REQUEST, "Existing User Not Found");

        when(restTemplate.getForObject(USER_SERVICE_BASE_URL + "/" + userId, UserResponseModel.class))
                .thenThrow(exception);

        assertThrows(ExistingUserNotFoundException.class, () -> userServiceClient.getUser(userId));
    }

    @Test
    void getUserByEmailReturnsUser() {
        String email = "johny.doey@example.com";

        UserResponseModel expectedUserResponseModel = UserResponseModel.builder()
                .firstName("Johnny")
                .lastName("Doeyy")
                .email("johny.doey@example.com")
                .phoneNumber("5147899876")
                .password("pasAWord")
                .build();

        when(restTemplate.getForObject(USER_SERVICE_BASE_URL + "/email?email=" + email, UserResponseModel.class))
                .thenReturn(expectedUserResponseModel);
        UserResponseModel actualUserResponseModel = userServiceClient.getUserByEmail(email);
        assertEquals(expectedUserResponseModel, actualUserResponseModel);
    }

    @Test
    void getUserByEmail_whenRestTemplateThrowsHttpClientErrorException_shouldThrowNotFoundException() {
        String email = "johny.doey@example.com";
        HttpClientErrorException exception = new HttpClientErrorException(NOT_FOUND, "Not found");

        when(restTemplate.getForObject(USER_SERVICE_BASE_URL + "/email?email=" + email, UserResponseModel.class))
                .thenThrow(exception);
        assertThrows(NotFoundException.class, () -> userServiceClient.getUserByEmail(email));
    }

    @Test
    void getUserByEmail_whenRestTemplateThrowsHttpClientErrorException_shouldThrowUnprocessableEntityException() {
        String email = "johny.doey@example.com";
        HttpClientErrorException exception = new HttpClientErrorException(UNPROCESSABLE_ENTITY, "Unprocessable Entity");

        when(restTemplate.getForObject(USER_SERVICE_BASE_URL + "/email?email=" + email, UserResponseModel.class))
                .thenThrow(exception);

        assertThrows(InvalidInputException.class, () -> userServiceClient.getUserByEmail(email));
    }

    @Test
    void getUserByEmail_whenRestTemplateThrowsHttpClientErrorException_shouldThrowExistingUserNotFoundException() {
        String email = "johny.doey@example.com";
        HttpClientErrorException exception = new HttpClientErrorException(BAD_REQUEST, "Existing User Not Found");

        when(restTemplate.getForObject(USER_SERVICE_BASE_URL + "/email?email=" + email, UserResponseModel.class))
                .thenThrow(exception);
        assertThrows(ExistingUserNotFoundException.class, () -> userServiceClient.getUserByEmail(email));
    }

    @Test
    void getUserByEmailAndPassWordReturnsUser() {
        String email = "johny.doey@example.com";
        String password = "pasAWord";

        UserResponseModel expectedUserResponseModel = UserResponseModel.builder()
                .firstName("Johnny")
                .lastName("Doeyy")
                .email("johny.doey@example.com")
                .phoneNumber("5147899876")
                .password("pasAWord")
                .build();

        when(restTemplate.postForObject(USER_SERVICE_BASE_URL + "/login", new UserLoginRequestModel(email, password), UserResponseModel.class))
                .thenReturn(expectedUserResponseModel);

        UserResponseModel actualUserResponseModel = userServiceClient.getUserByEmailAndPassword(email, password);
        assertEquals(expectedUserResponseModel, actualUserResponseModel);
    }

    @Test
    void getUserByEmailAndPassWord_whenRestTemplateThrowsHttpClientErrorException_shouldThrowNotFoundException() {
        String email = "johny.doey@example.com";
        String password = "pasAWord";

        HttpClientErrorException exception = new HttpClientErrorException(NOT_FOUND, "Not found");

        when(restTemplate.postForObject(USER_SERVICE_BASE_URL + "/login", new UserLoginRequestModel(email, password), UserResponseModel.class))
                .thenThrow(exception);

        assertThrows(NotFoundException.class, () -> userServiceClient.getUserByEmailAndPassword(email, password));
    }

    @Test
    void getUserByEmailAndPassWord_whenRestTemplateThrowsHttpClientErrorException_shouldThrowUnprocessableEntityException() {
        String email = "johny.doey@example.com";
        String password = "pasAWord";

        HttpClientErrorException exception = new HttpClientErrorException(UNPROCESSABLE_ENTITY, "Unprocessable Entity");

        when(restTemplate.postForObject(USER_SERVICE_BASE_URL + "/login", new UserLoginRequestModel(email, password), UserResponseModel.class))
                .thenThrow(exception);
        assertThrows(InvalidInputException.class, () -> userServiceClient.getUserByEmailAndPassword(email, password));

    }

    @Test
    void getUserByEmailAndPassWord_whenRestTemplateThrowsHttpClientErrorException_shouldThrowExistingUserNotFoundException() {
        String email = "johny.doey@example.com";
        String password = "pasAWord";

        HttpClientErrorException exception = new HttpClientErrorException(BAD_REQUEST, "Existing User Not Found");

        when(restTemplate.postForObject(USER_SERVICE_BASE_URL + "/login", new UserLoginRequestModel(email, password), UserResponseModel.class))
                .thenThrow(exception);
        assertThrows(ExistingUserNotFoundException.class, () -> userServiceClient.getUserByEmailAndPassword(email, password));
    }

    @Test
    void createUserReturnsUser() {
        UserRequestModel userRequestModel = buildUserRequestModel();

        UserResponseModel expectedUserResponseModel = new UserResponseModel();
        expectedUserResponseModel.setFirstName(userRequestModel.getFirstName());
        expectedUserResponseModel.setLastName(userRequestModel.getLastName());
        expectedUserResponseModel.setEmail(userRequestModel.getEmail());
        expectedUserResponseModel.setPhoneNumber(userRequestModel.getPhoneNumber());
        expectedUserResponseModel.setPassword(userRequestModel.getPassword());

        when(restTemplate.postForObject(USER_SERVICE_BASE_URL, userRequestModel, UserResponseModel.class))
                .thenReturn(expectedUserResponseModel);

        UserResponseModel actualUserResponseModel = userServiceClient.addUser(userRequestModel);
        assertEquals(expectedUserResponseModel, actualUserResponseModel);
        assertEquals(expectedUserResponseModel.getFirstName(), actualUserResponseModel.getFirstName());
        assertEquals(expectedUserResponseModel.getLastName(), actualUserResponseModel.getLastName());
        assertEquals(expectedUserResponseModel.getEmail(), actualUserResponseModel.getEmail());
        assertEquals(expectedUserResponseModel.getPhoneNumber(), actualUserResponseModel.getPhoneNumber());
        assertEquals(expectedUserResponseModel.getPassword(), actualUserResponseModel.getPassword());
    }

    @Test
    void createUser_whenRestTemplateThrowsHttpClientErrorException_shouldThrowNotFoundException() {
        UserRequestModel userRequestModel = buildUserRequestModel();

        HttpClientErrorException exception = new HttpClientErrorException(NOT_FOUND, "Not found");

        when(restTemplate.postForObject(USER_SERVICE_BASE_URL, userRequestModel, UserResponseModel.class))
                .thenThrow(exception);

        assertThrows(NotFoundException.class, () -> userServiceClient.addUser(userRequestModel));
    }

    @Test
    void createUser_whenRestTemplateThrowsHttpClientErrorException_shouldThrowUnprocessableEntityException() {
        UserRequestModel userRequestModel = buildUserRequestModel();

        HttpClientErrorException exception = new HttpClientErrorException(UNPROCESSABLE_ENTITY, "Unprocessable Entity");

        when(restTemplate.postForObject(USER_SERVICE_BASE_URL, userRequestModel, UserResponseModel.class))
                .thenThrow(exception);

        assertThrows(InvalidInputException.class, () -> userServiceClient.addUser(userRequestModel));
    }

    @Test
    void createUser_whenRestTemplateThrowsHttpClientErrorException_shouldThrowExistingUserNotFoundException() {
        UserRequestModel userRequestModel = buildUserRequestModel();

        HttpClientErrorException exception = new HttpClientErrorException(BAD_REQUEST, "Existing User Not Found");

        when(restTemplate.postForObject(USER_SERVICE_BASE_URL, userRequestModel, UserResponseModel.class))
                .thenThrow(exception);

        assertThrows(ExistingUserNotFoundException.class, () -> userServiceClient.addUser(userRequestModel));
    }

    @Test
    void updateUserReturnsUser() {
        String userId = "1234";
        UserRequestModel userRequestModel = buildUserRequestModel();

        UserResponseModel expectedUserResponseModel = UserResponseModel.builder()
                .firstName("Mila")
                .lastName("Kehayova")
                .email("mk.mk@example.com")
                .phoneNumber("5147913510")
                .password("catAndDog")
                .build();

        String updateUrl = USER_SERVICE_BASE_URL + "/" + userId;
        HttpEntity<UserRequestModel> userRequestModelHttpEntity = new HttpEntity<>(userRequestModel);

        when(restTemplate.exchange(updateUrl, HttpMethod.PUT, userRequestModelHttpEntity, UserResponseModel.class))
                .thenReturn(new ResponseEntity<>(expectedUserResponseModel, HttpStatus.OK));

        UserResponseModel actualUserResponseModel = userServiceClient.updateUser(userId, userRequestModel);
        assertEquals(expectedUserResponseModel, actualUserResponseModel);
    }

    @Test
    void updateUser_whenRestTemplateThrowsHttpClientErrorException_shouldThrowNotFoundException() {
        String userId = "1234";
        UserRequestModel userRequestModel = buildUserRequestModel();

        UserResponseModel expectedUserResponseModel = UserResponseModel.builder()
                .firstName("Mila")
                .lastName("Kehayova")
                .email("mk.mk@example.com")
                .phoneNumber("5147913510")
                .password("catAndDog")
                .build();

        String updateUrl = USER_SERVICE_BASE_URL + "/" + userId;
        HttpEntity<UserRequestModel> userRequestModelHttpEntity = new HttpEntity<>(userRequestModel);
        HttpClientErrorException exception = new HttpClientErrorException(NOT_FOUND, "Not found");

        when(restTemplate.exchange(updateUrl, HttpMethod.PUT, userRequestModelHttpEntity, UserResponseModel.class))
                .thenThrow(exception);
        assertThrows(NotFoundException.class, () -> userServiceClient.updateUser(userId, userRequestModel));
    }

    @Test
    void updateUser_whenRestTemplateThrowsHttpClientErrorException_shouldThrowUnprocessableEntityException() {
        String userId = "1234";
        UserRequestModel userRequestModel = buildUserRequestModel();
        HttpClientErrorException exception = new HttpClientErrorException(UNPROCESSABLE_ENTITY, "Unprocessable Entity");
        when(restTemplate.exchange(USER_SERVICE_BASE_URL + "/" + userId, HttpMethod.PUT, new HttpEntity<>(userRequestModel), UserResponseModel.class))
                .thenThrow(exception);
        assertThrows(InvalidInputException.class, () -> userServiceClient.updateUser(userId, userRequestModel));
    }

    @Test
    void updateUser_whenRestTemplateThrowsHttpClientErrorException_shouldThrowExistingUserNotFoundException() {
        String userId = "1234";
        UserRequestModel userRequestModel = buildUserRequestModel();
        HttpClientErrorException exception = new HttpClientErrorException(BAD_REQUEST, "Existing User Not Found");
        when(restTemplate.exchange(USER_SERVICE_BASE_URL + "/" + userId, HttpMethod.PUT, new HttpEntity<>(userRequestModel), UserResponseModel.class))
                .thenThrow(exception);
        assertThrows(ExistingUserNotFoundException.class, () -> userServiceClient.updateUser(userId, userRequestModel));
    }

    @Test
    void deleteGallery(){
        String userId = "1234";
        String deleteUrl = USER_SERVICE_BASE_URL + "/" + userId;

        userServiceClient.deleteUser(userId);
        Mockito.verify(restTemplate).delete(deleteUrl);
    }

    @Test
    void deleteUser_whenRestTemplateThrowsHttpClientErrorException_shouldThrowNotFoundException() {
        String userId = "1234";
        HttpClientErrorException exception = new HttpClientErrorException(NOT_FOUND, "Not found");
        Mockito.doThrow(exception).when(restTemplate).delete(USER_SERVICE_BASE_URL + "/" + userId);
        assertThrows(NotFoundException.class, () -> userServiceClient.deleteUser(userId));
    }

    @Test
    void deleteUser_whenRestTemplateThrowsHttpClientErrorException_shouldThrowUnprocessableEntityException() {
        String userId = "1234";
        HttpClientErrorException exception = new HttpClientErrorException(UNPROCESSABLE_ENTITY, "Unprocessable Entity");
        Mockito.doThrow(exception).when(restTemplate).delete(USER_SERVICE_BASE_URL + "/" + userId);
        assertThrows(InvalidInputException.class, () -> userServiceClient.deleteUser(userId));
    }

    @Test
    void deleteUser_whenRestTemplateThrowsHttpClientErrorException_shouldThrowExistingUserNotFoundException() {
        String userId = "1234";
        HttpClientErrorException exception = new HttpClientErrorException(BAD_REQUEST, "Existing User Not Found");
        Mockito.doThrow(exception).when(restTemplate).delete(USER_SERVICE_BASE_URL + "/" + userId);
        assertThrows(ExistingUserNotFoundException.class, () -> userServiceClient.deleteUser(userId));
    }

    private UserRequestModel buildUserRequestModel(){
        return UserRequestModel.builder()
                .firstName("Johnny")
                .lastName("Doeyy")
                .email("johny.doey@example.com")
                .phoneNumber("5147899876")
                .password("pasAWord")
                .build();
    }

}