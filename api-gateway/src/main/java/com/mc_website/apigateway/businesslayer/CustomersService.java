package com.mc_website.apigateway.businesslayer;

import com.mc_website.apigateway.presentation.CustomerRequestModel;
import com.mc_website.apigateway.presentation.CustomerResponseModel;

public interface CustomersService {
    CustomerResponseModel[] getCustomers();
    CustomerResponseModel getCustomerById(String customerId);
    CustomerResponseModel addCustomer(CustomerRequestModel customerRequestModel);
    CustomerResponseModel updateCustomer(String customerId, CustomerRequestModel customerRequestModel);
    void deleteCustomer(String customerId);
}
