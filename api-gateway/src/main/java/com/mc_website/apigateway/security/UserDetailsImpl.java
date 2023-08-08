package com.mc_website.apigateway.security;

import com.mc_website.apigateway.domainclientlayer.User.UserServiceClient;
import com.mc_website.apigateway.presentation.User.UserResponseModel;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsImpl implements UserDetailsService {

    private final UserServiceClient userServiceClient;

    public UserDetailsImpl(UserServiceClient userServiceClient) {
        this.userServiceClient = userServiceClient;
    }

    @Override
    public UserPrincipalImpl loadUserByUsername(String email) {
        UserResponseModel user = userServiceClient.getUserByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(email);
        }
        return new UserPrincipalImpl(user);
    }


}
