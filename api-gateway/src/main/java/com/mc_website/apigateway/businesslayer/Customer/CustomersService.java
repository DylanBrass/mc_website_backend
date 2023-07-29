package com.mc_website.apigateway.businesslayer.Customer;

import com.mc_website.apigateway.presentation.Customer.CustomerRequestModel;
import com.mc_website.apigateway.presentation.Customer.CustomerResponseModel;

public interface CustomersService {
    CustomerResponseModel[] getCustomers();
    CustomerResponseModel getCustomerById(String customerId);
    CustomerResponseModel addCustomer(CustomerRequestModel customerRequestModel);
    CustomerResponseModel updateCustomer(String customerId, CustomerRequestModel customerRequestModel);
    void deleteCustomer(String customerId);
}
