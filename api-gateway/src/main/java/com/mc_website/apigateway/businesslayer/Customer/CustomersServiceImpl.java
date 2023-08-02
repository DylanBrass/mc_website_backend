package com.mc_website.apigateway.businesslayer.Customer;
import com.mc_website.apigateway.domainclientlayer.Customer.CustomerServiceClient;
import com.mc_website.apigateway.presentation.Customer.CustomerRequestModel;
import com.mc_website.apigateway.presentation.Customer.CustomerResponseModel;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class CustomersServiceImpl implements CustomersService {

    private final CustomerServiceClient customerServiceClient;

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

    @Override
    public String customerForgotEmail() {
        return customerServiceClient.customerForgotPassword();
    }

    @Override
    public String sendEmailForForgottenEmail(HttpServletRequest request) {
        return customerServiceClient.sendForgottenEmail(request);
    }

    @Override
    public String resetPasswordPage(String token) {
        return customerServiceClient.customerShowResetPage(token);
    }

    @Override
    public String resetPassword(HttpServletRequest request) {
        return customerServiceClient.changePassword(request);
    }


}
