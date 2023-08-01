package com.mc_website.apigateway.businesslayer.Customer;

import com.mc_website.apigateway.presentation.Customer.CustomerRequestModel;
import com.mc_website.apigateway.presentation.Customer.CustomerResponseModel;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;

public interface CustomersService {
    CustomerResponseModel[] getCustomers();
    CustomerResponseModel getCustomerById(String customerId);
    CustomerResponseModel addCustomer(CustomerRequestModel customerRequestModel);
    CustomerResponseModel updateCustomer(String customerId, CustomerRequestModel customerRequestModel);
    CustomerResponseModel getCustomerByEmailAndPassword(String email, String password);
    CustomerResponseModel getCustomerByEmail(String email);
    void deleteCustomer(String customerId);
    String customerForgotEmail();
    String sendEmailForForgottenEmail(HttpServletRequest request);
}
