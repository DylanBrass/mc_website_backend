package com.mc_website.apigateway.businesslayer.User;

import com.mc_website.apigateway.presentation.User.UserRequestModel;
import com.mc_website.apigateway.presentation.User.UserResponseModel;
import jakarta.servlet.http.HttpServletRequest;

public interface UsersService {
    UserResponseModel[] getUsers();
    UserResponseModel getUserById(String userId);
    UserResponseModel addUser(UserRequestModel userRequestModel);
    UserResponseModel updateUser(String userId, UserRequestModel userRequestModel);
    UserResponseModel getUserByEmailAndPassword(String email, String password);
    UserResponseModel getUserByEmail(String email);
    void deleteUser(String userId);
    String userForgotEmail();
    String sendEmailForForgottenEmail(HttpServletRequest request);

    String resetPasswordPage(String token);

    String resetPassword(HttpServletRequest request);

}
