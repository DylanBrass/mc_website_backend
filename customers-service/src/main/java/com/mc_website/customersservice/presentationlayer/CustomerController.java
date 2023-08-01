package com.mc_website.customersservice.presentationlayer;

import com.mc_website.customersservice.businesslayer.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

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
    public String processForgotPassword(@RequestBody CustomerResetPwdRequestModel customerResetPwdRequestModel) {
        Model model = new ConcurrentModel();
        String email = customerResetPwdRequestModel.getEmail();
        String token = UUID.randomUUID().toString();
        log.debug(email);
        try {
            customerService.updateResetPasswordToken(token, email);
            String resetPasswordLink =  customerResetPwdRequestModel.getUrl()+ "/reset_password?token=" + token;
            sendEmail(email, resetPasswordLink);
            model.addAttribute("message", "We have sent a reset password link to your email. Please check.");

        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
        }

        return "forgot_password_form";
    }

    public void sendEmail(String recipientEmail, String link) throws MessagingException, UnsupportedEncodingException {
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
    public String showResetPasswordForm() {
return null;
    }

    @PostMapping("/reset_password")
    public String processResetPassword() {
        return null;

    }

}

