package com.mc_websiteusersservice.presentationlayer;

import com.mc_websiteusersservice.businesslayer.UserService;
import com.mc_websiteusersservice.datalayer.ResetPasswordToken;
import com.mc_websiteusersservice.datalayer.ResetPasswordTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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

    ResetPasswordTokenRepository resetPasswordTokenRepository;
    UserService userService;
    private final String username;
    private final String password;
    Session session;

    public UserController(@Value("${spring.mail.username}") String username, @Value("${spring.mail.password}") String password, UserService userService,ResetPasswordTokenRepository resetPasswordTokenRepository) {
        this.username = username;
        this.password = password;
        this.resetPasswordTokenRepository = resetPasswordTokenRepository;
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
            log.info("In the controller, before passing to the service impl, token: " + token + " email: " + email);

            token = BCrypt.hashpw(token, BCrypt.gensalt(10));
            userService.updateResetPasswordToken(token, email);
            ResetPasswordToken resetPasswordToken = resetPasswordTokenRepository.findResetPasswordTokenByToken(token);

            Map<String, Object> claims = new HashMap<>();
            claims.put("Token", resetPasswordToken.getToken());
            claims.put("Expire date", resetPasswordToken.getExpiryDate());
            claims.put("User", resetPasswordToken.getUserIdentifier().getUserId());

            String jwToken = Jwts.builder()
            .setClaims(claims)
                    .setExpiration(resetPasswordToken.getExpiryDate())
                    .signWith(SignatureAlgorithm.HS512, "PasswordResetSecret" )
                    .compact();

            String resetPasswordLink =  userResetPwdRequestModel.getUrl()+ "/api/v1/users/reset_password?token=" + jwToken;
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
        //Hash token
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

        Claims claims = Jwts.parser()
                .setSigningKey( "PasswordResetSecret")
                .parseClaimsJws(token)
                .getBody();

        String tokenStr = claims.get("token").toString();
        log.info("JWT token payload: " + claims);
        //Hash token
        UserResponseModel userResponseModel = userService.getByResetPasswordToken(tokenStr);




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

