package com.mc_website.customersservice.businesslayer;

import com.mc_website.customersservice.datalayer.ResetPasswordToken;
import com.mc_website.customersservice.presentationlayer.CustomerRequestModel;
import com.mc_website.customersservice.presentationlayer.CustomerResponseModel;
import org.apache.el.parser.Token;

import java.util.List;

public interface CustomerService {
    List<CustomerResponseModel> getCustomers();
    CustomerResponseModel getCustomerById(String customerId);
    CustomerResponseModel addCustomer(CustomerRequestModel customerRequestModel);
    CustomerResponseModel updateCustomer(String customerId, CustomerRequestModel customerRequestModel);

    CustomerResponseModel getCustomerByEmail(String email);
    CustomerResponseModel getCustomerByEmailAndPassword(String email, String password);
    void deleteCustomer(String customerId);

    void updateResetPasswordToken(String token, String email);

    CustomerResponseModel getByResetPasswordToken(String token);

    void updatePassword(String newPassword, String token);

}
