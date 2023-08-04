package com.mc_website.apigateway.presentation.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserLoginRequestModel {
    String email;
    String password;
}
