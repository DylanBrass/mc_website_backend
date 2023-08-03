package com.mc_website.customersservice.presentationlayer;

import com.mc_website.customersservice.businesslayer.CustomerService;
import com.mc_website.customersservice.datalayer.ResetPasswordToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.el.parser.Token;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("api/v1/customers")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class CustomerController {

    CustomerService customerService;
    private final String username;
    private final String password;
    Session session;

    public CustomerController(@Value("${spring.mail.username}") String username, @Value("${spring.mail.password}") String password,CustomerService customerService) {
        this.username = username;
        this.password = password;

        this.customerService = customerService;
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
    public ResponseEntity<List<CustomerResponseModel>> getCustomers(){
        return ResponseEntity.ok(customerService.getCustomers());
    }
    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerResponseModel> getCustomerById(@PathVariable String customerId){
        return ResponseEntity.ok(customerService.getCustomerById(customerId));
    }

    // Get customer by email
    @GetMapping("/email")
    public ResponseEntity<CustomerResponseModel> getCustomerByEmail(@RequestParam("email") String email){
        return ResponseEntity.ok(customerService.getCustomerByEmail(email));
    }

    @PostMapping()
    public ResponseEntity<CustomerResponseModel> addCustomer(@RequestBody CustomerRequestModel customerRequestModel){
        return ResponseEntity.ok(customerService.addCustomer(customerRequestModel));
    }
    @PostMapping("/login")
    public ResponseEntity<CustomerResponseModel> getCustomerByEmailAndPassword(@RequestBody CustomerLoginRequestModel customerRequestModel){
        return ResponseEntity.ok(customerService.getCustomerByEmailAndPassword(customerRequestModel.getEmail(), customerRequestModel.getPassword()));
    }
    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerResponseModel> updateCustomer(@PathVariable String customerId, @RequestBody CustomerRequestModel customerRequestModel){
        return ResponseEntity.ok(customerService.updateCustomer(customerId, customerRequestModel));
    }
    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable String customerId){
        customerService.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/forgot_password")
    public String showForgotPasswordForm() {
            return "forgot_password_form";
    }

    @PostMapping("/forgot_password")
    public String processForgotPassword(@RequestBody CustomerResetPwdRequestModel customerResetPwdRequestModel,Model model) {
        String email = customerResetPwdRequestModel.getEmail();
        String token = UUID.randomUUID().toString();
        try {
            customerService.getCustomerByEmail(email);
        }
        catch(RuntimeException e){
        model.addAttribute("message", "This Email is not registered to any account !");
        return "forgot_password_form";
    }

        try {
            customerService.updateResetPasswordToken(token, email);
            String resetPasswordLink =  customerResetPwdRequestModel.getUrl()+ "/api/v1/customers/reset_password?token=" + token;
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


        CustomerResponseModel customerResponseModel = customerService.getByResetPasswordToken(token);
        model.addAttribute("token", token);

        if (customerResponseModel == null) {
            model.addAttribute("message", "Invalid Token");
            return "message";
        }

        return "reset_password_form";

    }

    @PostMapping("/reset_password")
    public String processResetPassword(@RequestBody CustomerResetPwdWithTokenRequestModel resetRequest,Model model) {
        String token = resetRequest.getToken();
        String password = resetRequest.getPassword();
        CustomerResponseModel customerResponseModel = customerService.getByResetPasswordToken(token);
        model.addAttribute("title", "Reset your password");

        if (customerResponseModel == null) {
            model.addAttribute("message", "Invalid Token");
            return "message";
        } else {
            customerService.updatePassword(password, token);

            model.addAttribute("message", "You have successfully changed your password.");
        }

        return "message";
    }

}

