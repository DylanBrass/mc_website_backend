package com.mc_website.customersservice.presentationlayer;

import com.mc_website.customersservice.businesslayer.CustomerService;
import com.mc_website.customersservice.datalayer.Customer;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
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

    @GetMapping
    public ResponseEntity<List<CustomerResponseModel>> getCustomers(){
        return ResponseEntity.ok(customerService.getCustomers());
    }
    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerResponseModel> getCustomerById(@PathVariable String customerId){
        return ResponseEntity.ok(customerService.getCustomerById(customerId));
    }
    @PostMapping()
    public ResponseEntity<CustomerResponseModel> addCustomer(@RequestBody CustomerRequestModel customerRequestModel){
        return ResponseEntity.ok(customerService.addCustomer(customerRequestModel));
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

    /*@PostMapping("/resetPassword")
    public ResponseEntity<Void> resetPassword(HttpServletRequest request,
                                        @RequestParam("email") String email) {
        CustomerResponseModel customer = customerService.getCustomerByEmail(email);

        String token = UUID.randomUUID().toString();
        customerService.createPasswordResetTokenForUser(customer, token);
        mailSender.send(constructResetTokenEmail(getAppUrl(request),
                request.getLocale(), token, customer));
        return ResponseEntity.ok().build();
    }*/

    /*private SimpleMailMessage constructResetTokenEmail(
            String contextPath, Locale locale, String token, CustomerResponseModel customer) {
        String url = contextPath + "/user/changePassword?token=" + token;
        String message = messages.getMessage("message.resetPassword",
                null, locale);
        return constructEmail("Reset Password", message + " \r\n" + url, customer);
    }

    private SimpleMailMessage constructEmail(String subject, String body,
                                             CustomerResponseModel customer) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(customer.getEmail());
        email.setFrom(env.getProperty("support.email"));
        return email;
    }*/


}
