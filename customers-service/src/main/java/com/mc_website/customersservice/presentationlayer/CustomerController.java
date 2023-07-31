package com.mc_website.customersservice.presentationlayer;

import com.mc_website.customersservice.businesslayer.CustomerService;
import com.mc_website.customersservice.datalayer.Customer;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/customers")
@CrossOrigin(origins = "http://localhost:3000")
public class CustomerController {

    CustomerService customerService;
    MailSender mailSender;
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
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
    @GetMapping("/email")//Why ?????
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
        return null;

    }

    @PostMapping("/forgot_password")
    public String processForgotPassword() {
        return null;

    }

    public void sendEmail(){

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
