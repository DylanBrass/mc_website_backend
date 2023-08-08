package com.mc_website.apigateway.presentation.User;

import com.mc_website.apigateway.businesslayer.User.UsersService;
import com.mc_website.apigateway.datamapperlayer.UserResponseModelLessPasswordMapper;
import com.mc_website.apigateway.security.JwtTokenUtil;
import com.mc_website.apigateway.security.UserPrincipalImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/users")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class UserController {
    private final PasswordEncoder passwordEncoder;
    private final UsersService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserResponseModelLessPasswordMapper removePwdMapper;

    public UserController(PasswordEncoder passwordEncoder, UsersService userService,
                          AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil,
                          UserResponseModelLessPasswordMapper removePwdMapper) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.removePwdMapper = removePwdMapper;
    }

    @GetMapping
    public ResponseEntity<UserResponseModelPasswordLess[]> getAllUsers(){
        return ResponseEntity.ok(removePwdMapper.responseModelsToResponseModelsLessPassword(userService.getUsers()));
    }
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseModelPasswordLess> getUserById(@PathVariable String userId){
        return ResponseEntity.ok(removePwdMapper.responseModelToResponseModelLessPassword(userService.getUserById(userId)));
    }

    @GetMapping("/email")
    public ResponseEntity<UserResponseModelPasswordLess> getUserByEmail(@RequestParam("email") String email){
        return ResponseEntity.ok(removePwdMapper.responseModelToResponseModelLessPassword(userService.getUserByEmail(email)));
    }

    @PostMapping()
    public ResponseEntity<UserResponseModelPasswordLess> addUser(@RequestBody UserRequestModel userRequestModel){
        userRequestModel.setPassword(passwordEncoder.encode(userRequestModel.getPassword()));
        return ResponseEntity.ok(removePwdMapper.responseModelToResponseModelLessPassword(userService.addUser(userRequestModel)));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseModelPasswordLess> loginUser(@RequestBody UserLoginRequestModel login){
        try {
            Authentication authenticate = authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    login.getEmail(), login.getPassword()
                            )
                    );

            UserPrincipalImpl user = (UserPrincipalImpl) authenticate.getPrincipal();

            return ResponseEntity.ok()
                    .header(
                            HttpHeaders.AUTHORIZATION,
                            jwtTokenUtil.generateToken(user)
                    )
                    .body(removePwdMapper.responseModelToResponseModelLessPassword(userService.getUserByEmail(user.getUsername())));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    @PutMapping("/{userId}")
    public ResponseEntity<UserResponseModelPasswordLess> updateUser(@PathVariable String userId, @RequestBody UserRequestModel userRequestModel){
        return ResponseEntity.ok(removePwdMapper.responseModelToResponseModelLessPassword(userService.updateUser(userId, userRequestModel)));
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
