package com.mc_website.apigateway.presentation.User;

import com.mc_website.apigateway.businesslayer.User.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/users")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class UserController {
    UsersService userService;
    public UserController(UsersService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<UserResponseModel[]> getAllUsers(){
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
    public ResponseEntity<UserResponseModel> loginUser(@RequestBody UserRequestModel userRequestModel){
        return ResponseEntity.ok(userService.getUserByEmailAndPassword(userRequestModel.getEmail(), userRequestModel.getPassword()));
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

    @GetMapping("/forgot_password")
    public String showForgotPasswordForm() {
        return userService.userForgotEmail();
    }


    @PostMapping("/forgot_password")
    public String processForgotPassword(HttpServletRequest request) {
        return userService.sendEmailForForgottenEmail(request);

    }

    @GetMapping("/reset_password")
    public String showResetPasswordForm(@Param(value = "token") String token) throws IllegalAccessException {
        if(token == null)
            throw new IllegalAccessException("An error as occured");


        return userService.resetPasswordPage(token);

    }

    @PostMapping("/reset_password")
    public String processResetPassword(HttpServletRequest resetRequest) {
        return userService.resetPassword(resetRequest);
    }

}
