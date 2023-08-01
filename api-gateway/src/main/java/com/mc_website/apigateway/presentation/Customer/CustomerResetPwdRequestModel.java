package com.mc_website.apigateway.presentation.Customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerResetPwdRequestModel {
    private String email;
    private String url;
}
