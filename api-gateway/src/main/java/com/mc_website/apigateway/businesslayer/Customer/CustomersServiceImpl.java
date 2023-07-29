package com.mc_website.apigateway.businesslayer.Customer;
import com.mc_website.apigateway.domainclientlayer.Customer.CustomerServiceClient;
import com.mc_website.apigateway.presentation.Customer.CustomerRequestModel;
import com.mc_website.apigateway.presentation.Customer.CustomerResponseModel;
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
    public CustomerResponseModel getCustomerByEmailAndPassword(String email, String password) {
        return customerServiceClient.getCustomerByEmailAndPassword(email, password);
    }

    @Override
    public CustomerResponseModel getCustomerByEmail(String email) {
        return customerServiceClient.getCustomerByEmail(email);
    }

    @Override
    public void deleteCustomer(String customerId) {
        customerServiceClient.deleteCustomer(customerId);
    }
}
