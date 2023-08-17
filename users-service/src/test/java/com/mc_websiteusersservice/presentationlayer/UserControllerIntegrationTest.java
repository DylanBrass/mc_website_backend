package com.mc_websiteusersservice.presentationlayer;

import com.mc_websiteusersservice.datalayer.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Slf4j
@SpringBootTest(webEnvironment = RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserControllerIntegrationTest {

    private final String BASE_URI_USERS = "/api/v1/users";
    private final String VALID_USER_ID = "c121217a-a1c0-4f7f-a5be-f3d0c6d6ed21";
    private final String VALID_USER_FIRST_NAME = "Milaaaaaa";
    private final String VALID_USER_LAST_NAME = "Kehayova";
    private final String VALID_USER_EMAIL = "mila.kehayova@example.com";
    private final String VALID_USER_PHONE_NUMBER = "1234567890";
    private final String VALID_USER_PASSWORD = "Mkijh123$";

    @Autowired
    WebTestClient webTestClient;
    @Autowired
    UserRepository userRepository;

    @Test
    public void whenUsersExist_thenReturnAllUsers() {
        // arrange
        long expectedNumberOfUsers = userRepository.count();

        // act
        webTestClient.get()
                .uri(BASE_URI_USERS)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserResponseModel.class)
                .hasSize((int) expectedNumberOfUsers);
    }
}