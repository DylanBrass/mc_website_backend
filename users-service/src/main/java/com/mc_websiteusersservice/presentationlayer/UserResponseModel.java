package com.mc_websiteusersservice.presentationlayer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserResponseModel {
    String userId;
    String firstName;
    String lastName;
    String password;
    String email;
    String phoneNumber;
}
