package com.mc_websiteusersservice.datalayer;

import com.mc_websiteusersservice.presentationlayer.UserResponseModel;
import jakarta.persistence.*;
import lombok.Data;

@Table(name="users")
@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    @Embedded
    private UserIdentifier userIdentifier;

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;


    //add a when the resetPasswordToken was created time to make it expire
    public User(){
        this.userIdentifier = new UserIdentifier();
    }

    public User(Integer id, UserIdentifier userIdentifier, String firstName, String lastName, String email, String phoneNumber, String password) {
        Id = id;
        this.userIdentifier = userIdentifier;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public User(UserResponseModel user) {
        this.userIdentifier = new UserIdentifier();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
    }
}
