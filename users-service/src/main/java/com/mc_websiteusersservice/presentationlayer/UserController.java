package com.mc_websiteusersservice.presentationlayer;

import com.mc_websiteusersservice.businesslayer.UserService;
import com.mc_websiteusersservice.datalayer.ResetPasswordToken;
import com.mc_websiteusersservice.datalayer.ResetPasswordTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Controller
@RequestMapping("api/v1/users")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class UserController {

    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<List<UserResponseModel>> getAllUsers(){
        return ResponseEntity.ok(userService.getUsers());
    }
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseModel> getUserById(@PathVariable String userId){
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @GetMapping("/email")
    public ResponseEntity<UserResponseModel> getUserByEmail(@RequestParam("email") String email){
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @PostMapping()
    public ResponseEntity<UserResponseModel> addUser(@RequestBody UserRequestModel userRequestModel){
        return ResponseEntity.ok(userService.addUser(userRequestModel));
    }
    @PostMapping("/login")
    public ResponseEntity<UserResponseModel> getUserByEmailAndPassword(@RequestBody UserLoginRequestModel userLoginRequestModel){
        return ResponseEntity.ok(userService.getUserByEmailAndPassword(userLoginRequestModel.getEmail(), userLoginRequestModel.getPassword()));
    }
    @PutMapping("/{userId}")
    public ResponseEntity<UserResponseModel> updateUser(@PathVariable String userId, @RequestBody UserRequestModel userRequestModel){
        return ResponseEntity.ok(userService.updateUser(userId, userRequestModel));
    }
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId){
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    //CAN BE DONE IN FRONT END
    @GetMapping("/forgot_password")
    public String showForgotPasswordForm() {
        return "forgot_password_form";
    }

    @PostMapping("/forgot_password")
    public String processForgotPassword(@RequestBody UserResetPwdRequestModel userResetPwdRequestModel, Model model) {
        //PUT IN A SERVICE
        model = userService.processForgotPassword(userResetPwdRequestModel, model);
        return "forgot_password_form";

    }



    @GetMapping("/reset_password")
    public String showResetPasswordForm(@RequestParam Map<String, String> querryParams, Model model) {
        //Hash token
       model = userService.showResetPasswordForm(querryParams, model);

        return "reset_password_form";

    }

    @PostMapping("/reset_password")
    public String processResetPassword(@RequestBody UserResetPwdWithTokenRequestModel resetRequest, Model model) {
       model = userService.processResetPassword(resetRequest, model);

        return "message";
    }

}

