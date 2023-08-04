package com.mc_websiteusersservice.presentationlayer;

import com.mc_websiteusersservice.businesslayer.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
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
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

@Controller
@RequestMapping("api/v1/customers")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class UserController {

    UserService userService;
    private final String username;
    private final String password;
    Session session;

    public UserController(@Value("${spring.mail.username}") String username, @Value("${spring.mail.password}") String password, UserService userService) {
        this.username = username;
        this.password = password;

        this.userService = userService;
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS

        session = javax.mail.Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                        return new javax.mail.PasswordAuthentication(username, password);
                    }
                });
    }

    @GetMapping()
    public ResponseEntity<List<UserResponseModel>> getCustomers(){
        return ResponseEntity.ok(userService.getUsers());
    }
    @GetMapping("/{customerId}")
    public ResponseEntity<UserResponseModel> getCustomerById(@PathVariable String customerId){
        return ResponseEntity.ok(userService.getUserById(customerId));
    }

    // Get customer by email
    @GetMapping("/email")
    public ResponseEntity<UserResponseModel> getCustomerByEmail(@RequestParam("email") String email){
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @PostMapping()
    public ResponseEntity<UserResponseModel> addCustomer(@RequestBody UserRequestModel userRequestModel){
        return ResponseEntity.ok(userService.addUser(userRequestModel));
    }
    @PostMapping("/login")
    public ResponseEntity<UserResponseModel> getCustomerByEmailAndPassword(@RequestBody UserLoginRequestModel customerRequestModel){
        return ResponseEntity.ok(userService.getUserByEmailAndPassword(customerRequestModel.getEmail(), customerRequestModel.getPassword()));
    }
    @PutMapping("/{customerId}")
    public ResponseEntity<UserResponseModel> updateCustomer(@PathVariable String customerId, @RequestBody UserRequestModel userRequestModel){
        return ResponseEntity.ok(userService.updateUser(customerId, userRequestModel));
    }
    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable String customerId){
        userService.deleteUser(customerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/forgot_password")
    public String showForgotPasswordForm() {
            return "forgot_password_form";
    }

    @PostMapping("/forgot_password")
    public String processForgotPassword(@RequestBody UserResetPwdRequestModel userResetPwdRequestModel, Model model) {
        String email = userResetPwdRequestModel.getEmail();
        String token = UUID.randomUUID().toString();
        try {
            userService.getUserByEmail(email);
        }
        catch(RuntimeException e){
        model.addAttribute("message", "This Email is not registered to any account !");
        return "forgot_password_form";
    }

        try {
            userService.updateResetPasswordToken(token, email);
            String resetPasswordLink =  userResetPwdRequestModel.getUrl()+ "/api/v1/customers/reset_password?token=" + token;
            sendEmail(email, resetPasswordLink);
            model.addAttribute("message", "We have sent a reset password link to your email. Please check.");

        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
        }

        return "forgot_password_form";
    }

    public void sendEmail(String recipientEmail, String link) throws MessagingException, UnsupportedEncodingException, InterruptedException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(recipientEmail) //grif2004@hotmail.com || kehayova.mila@gmail.com
        );
        message.setSubject("Change Password");
        message.setText("Test : " + link);

        Transport.send(message);
    }


    @GetMapping("/reset_password")
    public String showResetPasswordForm(@RequestParam Map<String, String> querryParams, Model model) {

        String token = querryParams.get("token");


        UserResponseModel userResponseModel = userService.getByResetPasswordToken(token);
        model.addAttribute("token", token);

        if (userResponseModel == null) {
            model.addAttribute("message", "Invalid Token");
            return "message";
        }

        return "reset_password_form";

    }

    @PostMapping("/reset_password")
    public String processResetPassword(@RequestBody UserResetPwdWithTokenRequestModel resetRequest, Model model) {
        String token = resetRequest.getToken();
        String password = resetRequest.getPassword();
        UserResponseModel userResponseModel = userService.getByResetPasswordToken(token);
        model.addAttribute("title", "Reset your password");

        if (userResponseModel == null) {
            model.addAttribute("message", "Invalid Token");
            return "message";
        } else {
            userService.updatePassword(password, token);

            model.addAttribute("message", "You have successfully changed your password.");
        }

        return "message";
    }

}

