package com.mc_website.apigateway.businesslayer.User;
import com.mc_website.apigateway.domainclientlayer.User.UserServiceClient;
import com.mc_website.apigateway.presentation.User.UserRequestModel;
import com.mc_website.apigateway.presentation.User.UserResponseModel;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class UsersServiceImpl implements UsersService {

    private final UserServiceClient userServiceClient;

    public UsersServiceImpl(UserServiceClient userServiceClient) {
        this.userServiceClient = userServiceClient;
    }

    @Override
    public UserResponseModel[] getUsers() {
        return userServiceClient.getAllUsers();
    }

    @Override
    public UserResponseModel getUserById(String userId) {
        return userServiceClient.getUser(userId);
    }

    @Override
    public UserResponseModel addUser(UserRequestModel userRequestModel) {
        return userServiceClient.addUser(userRequestModel);
    }

    @Override
    public UserResponseModel updateUser(String userId, UserRequestModel userRequestModel) {
        return userServiceClient.updateUser(userId, userRequestModel);
    }

    @Override
    public UserResponseModel getUserByEmailAndPassword(String email, String password) {
        return userServiceClient.getUserByEmailAndPassword(email, password);
    }

    @Override
    public UserResponseModel getUserByEmail(String email) {
        return userServiceClient.getUserByEmail(email);
    }

    @Override
    public void deleteUser(String userId) {
        userServiceClient.deleteUser(userId);
    }

    @Override
    public String userForgotEmail() {
        return userServiceClient.userForgotPassword();
    }

    @Override
    public String sendEmailForForgottenEmail(HttpServletRequest request) {
        return userServiceClient.sendForgottenEmail(request);
    }

    @Override
    public String resetPasswordPage(String token) {
        return userServiceClient.userShowResetPage(token);
    }

    @Override
    public String resetPassword(HttpServletRequest request) {
        return userServiceClient.changePassword(request);
    }


}
