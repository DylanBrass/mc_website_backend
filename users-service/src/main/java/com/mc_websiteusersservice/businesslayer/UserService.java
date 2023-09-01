package com.mc_websiteusersservice.businesslayer;

import com.mc_websiteusersservice.presentationlayer.UserRequestModel;
import com.mc_websiteusersservice.presentationlayer.UserResetPwdRequestModel;
import com.mc_websiteusersservice.presentationlayer.UserResetPwdWithTokenRequestModel;
import com.mc_websiteusersservice.presentationlayer.UserResponseModel;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;

public interface UserService {
    List<UserResponseModel> getUsers();
    UserResponseModel getUserById(String userId);
    UserResponseModel addUser(UserRequestModel userRequestModel);
    UserResponseModel updateUser(String userId, UserRequestModel userRequestModel);

    UserResponseModel getUserByEmail(String email);
    UserResponseModel getUserByEmailAndPassword(String email, String password);
    void deleteUser(String userId);

    Model processForgotPassword(UserResetPwdRequestModel userResetPwdWithTokenRequestModel, Model model);

    void updateResetPasswordToken(String token, String email);

    UserResponseModel getByResetPasswordToken(String token);

    void updatePassword(String newPassword, String token);

    Model showResetPasswordForm(Map<String, String> querryParams, Model model);

    Model processResetPassword(UserResetPwdWithTokenRequestModel resetRequest, Model model);
}
