package com.mc_website.apigateway.presentation.User;

import com.mc_website.apigateway.businesslayer.User.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/customers")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class UserController {
    UsersService customerService;
    public UserController(UsersService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<UserResponseModel[]> getCustomers(){
        return ResponseEntity.ok(customerService.getUsers());
    }
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseModel> getCustomerById(@PathVariable String userId){
        return ResponseEntity.ok(customerService.getUserById(userId));
    }

    @GetMapping("/email")
    public ResponseEntity<UserResponseModel> getCustomerByEmail(@RequestParam("email") String email){
        return ResponseEntity.ok(customerService.getUserByEmail(email));
    }

    @PostMapping()
    public ResponseEntity<UserResponseModel> addCustomer(@RequestBody UserRequestModel userRequestModel){
        return ResponseEntity.ok(customerService.addUser(userRequestModel));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseModel> loginCustomer(@RequestBody UserRequestModel userRequestModel){
        return ResponseEntity.ok(customerService.getUserByEmailAndPassword(userRequestModel.getEmail(), userRequestModel.getPassword()));
    }
    @PutMapping("/{userId}")
    public ResponseEntity<UserResponseModel> updateCustomer(@PathVariable String userId, @RequestBody UserRequestModel userRequestModel){
        return ResponseEntity.ok(customerService.updateUser(userId, userRequestModel));
    }
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable String userId){
        customerService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/forgot_password")
    public String showForgotPasswordForm() {
        return customerService.userForgotEmail();
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
