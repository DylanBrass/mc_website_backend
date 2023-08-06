package com.mc_websiteusersservice.businesslayer;

import com.mc_websiteusersservice.presentationlayer.UserRequestModel;
import com.mc_websiteusersservice.presentationlayer.UserResponseModel;

import java.util.List;

public interface UserService {
    List<UserResponseModel> getUsers();
    UserResponseModel getUserById(String userId);
    UserResponseModel addUser(UserRequestModel userRequestModel);
    UserResponseModel updateUser(String userId, UserRequestModel userRequestModel);

    UserResponseModel getUserByEmail(String email);
    UserResponseModel getUserByEmailAndPassword(String email, String password);
    void deleteUser(String userId);

    void updateResetPasswordToken(String token, String email);

    UserResponseModel getByResetPasswordToken(String token);

    void updatePassword(String newPassword, String token);

}