package com.mc_website.apigateway.presentation.Customer;

import com.mc_website.apigateway.businesslayer.Customer.CustomersService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/customers")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class CustomerController {
    CustomersService customerService;
    public CustomerController(CustomersService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<CustomerResponseModel[]> getCustomers(){
        return ResponseEntity.ok(customerService.getCustomers());
    }
    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerResponseModel> getCustomerById(@PathVariable String customerId){
        return ResponseEntity.ok(customerService.getCustomerById(customerId));
    }

    @GetMapping("/email")
    public ResponseEntity<CustomerResponseModel> getCustomerByEmail(@RequestParam("email") String email){
        return ResponseEntity.ok(customerService.getCustomerByEmail(email));
    }

    @PostMapping()
    public ResponseEntity<CustomerResponseModel> addCustomer(@RequestBody CustomerRequestModel customerRequestModel){
        return ResponseEntity.ok(customerService.addCustomer(customerRequestModel));
    }

    @PostMapping("/login")
    public ResponseEntity<CustomerResponseModel> loginCustomer(@RequestBody CustomerRequestModel customerRequestModel){
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
        return customerService.customerForgotEmail();
    }


    @PostMapping("/forgot_password")
    public String processForgotPassword(HttpServletRequest request) {
        return customerService.sendEmailForForgottenEmail(request);

    }

    @GetMapping("/reset_password")
    public String showResetPasswordForm(@Param(value = "token") String token) throws IllegalAccessException {
        if(token == null)
            throw new IllegalAccessException("An error as occured");
        return customerService.resetPasswordPage(token);

    }

    @PostMapping("/reset_password")
    public String processResetPassword(HttpServletRequest resetRequest) {
        return customerService.resetPassword(resetRequest);
    }

}
