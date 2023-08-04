package com.mc_websiteusersservice.datalayer;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name="reset_password_token")
@Data
public class ResetPasswordToken {
    private static final int EXPIRATION = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String token;

    @Embedded
    private UserIdentifier userIdentifier;

    private Date expiryDate;

    public ResetPasswordToken(String customerId, String token) {
        this.userIdentifier = new UserIdentifier(customerId);
        this.token = token;
        Calendar date = Calendar.getInstance();
        long timeInSecs = date.getTimeInMillis();
        Date expiryDate = new Date(timeInSecs + (15 * 60 * 1000));
        this.expiryDate = expiryDate;
    }

    public ResetPasswordToken() {

    }
}
