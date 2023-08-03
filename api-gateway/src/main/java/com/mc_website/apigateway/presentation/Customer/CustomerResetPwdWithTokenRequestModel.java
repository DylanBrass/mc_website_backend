package com.mc_website.apigateway.presentation.Customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerResetPwdWithTokenRequestModel {
    private String password;
    private String token;
}