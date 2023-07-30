package com.mc_website.apigateway.businesslayer;
import com.mc_website.apigateway.domainclientlayer.CustomerServiceClient;
import com.mc_website.apigateway.presentation.CustomerRequestModel;
import com.mc_website.apigateway.presentation.CustomerResponseModel;
import org.springframework.stereotype.Service;

@Service
public class CustomersServiceImpl implements CustomersService {

    private CustomerServiceClient customerServiceClient;

    public CustomersServiceImpl(CustomerServiceClient customerServiceClient) {
        this.customerServiceClient = customerServiceClient;
    }

    @Override
    public CustomerResponseModel[] getCustomers() {
        return customerServiceClient.getAllCustomers();
    }

    @Override
    public CustomerResponseModel getCustomerById(String customerId) {
        return customerServiceClient.getCustomer(customerId);
    }

    @Override
    public CustomerResponseModel addCustomer(CustomerRequestModel customerRequestModel) {
        return customerServiceClient.addCustomer(customerRequestModel);
    }

    @Override
    public CustomerResponseModel updateCustomer(String customerId, CustomerRequestModel customerRequestModel) {
        return customerServiceClient.updateCustomer(customerId, customerRequestModel);
    }

    @Override
    public void deleteCustomer(String customerId) {
        customerServiceClient.deleteCustomer(customerId);
    }
}
