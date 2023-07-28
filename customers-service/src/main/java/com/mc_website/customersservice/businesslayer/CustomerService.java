package com.mc_website.customersservice.businesslayer;

import com.mc_website.customersservice.presentationlayer.CustomerRequestModel;
import com.mc_website.customersservice.presentationlayer.CustomerResponseModel;

import java.util.List;

public interface CustomerService {
    List<CustomerResponseModel> getCustomers();
    CustomerResponseModel getCustomerById(String customerId);
    CustomerResponseModel addCustomer(CustomerRequestModel customerRequestModel);
    CustomerResponseModel updateCustomer(String customerId, CustomerRequestModel customerRequestModel);

    CustomerResponseModel getCustomerByEmail(String email);
    void resetPassword();
    void deleteCustomer(String customerId);

    void createPasswordResetTokenForUser(CustomerResponseModel customer, String token);
}
