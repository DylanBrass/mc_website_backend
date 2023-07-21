package com.mc_website.customersservice.businesslayer;

import com.mc_website.customersservice.datalayer.Customer;
import com.mc_website.customersservice.datalayer.CustomerRepository;
import com.mc_website.customersservice.datamapperlayer.CustomerRequestMapper;
import com.mc_website.customersservice.datamapperlayer.CustomerResponseMapper;
import com.mc_website.customersservice.presentationlayer.CustomerRequestModel;
import com.mc_website.customersservice.presentationlayer.CustomerResponseModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {
    private CustomerRepository customerRepository;
    private CustomerResponseMapper customerResponseMapper;
    private CustomerRequestMapper customerRequestMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerResponseMapper customerResponseMapper, CustomerRequestMapper customerRequestMapper) {
        this.customerRepository = customerRepository;
        this.customerResponseMapper = customerResponseMapper;
        this.customerRequestMapper = customerRequestMapper;
    }
    @Override
    public List<CustomerResponseModel> getCustomers() {
        return customerResponseMapper.entityListToResponseModelList(customerRepository.findAll());
    }

    @Override
    public CustomerResponseModel getCustomerById(String customerId) {
        if(!customerRepository.existsByCustomerIdentifier_CustomerId(customerId))
            throw new RuntimeException("Customer with id: " + customerId + " does not exist");
        return customerResponseMapper.entityToResponseModel(customerRepository.findByCustomerIdentifier_CustomerId(customerId));
    }

    @Override
    public CustomerResponseModel addCustomer(CustomerRequestModel customerRequestModel) {
        Customer customer = customerRepository.save(customerRequestMapper.requestModelToEntity(customerRequestModel));
        return customerResponseMapper.entityToResponseModel(customer);
    }

    @Override
    public CustomerResponseModel updateCustomer(String customerId, CustomerRequestModel customerRequestModel) {
        Customer customer = customerRequestMapper.requestModelToEntity(customerRequestModel);
        Customer existingCustomer = customerRepository.findByCustomerIdentifier_CustomerId(customerId);
        if(!customerRepository.existsByCustomerIdentifier_CustomerId(customerId))
            throw new RuntimeException("Customer with id: " + customerId + " does not exist");
        customer.setId(existingCustomer.getId());
        customer.setCustomerIdentifier(existingCustomer.getCustomerIdentifier());
        Customer updatedCustomer = customerRepository.save(customer);
        return customerResponseMapper.entityToResponseModel(updatedCustomer);
    }

    @Override
    public void deleteCustomer(String customerId) {
        if(!customerRepository.existsByCustomerIdentifier_CustomerId(customerId))
            throw new RuntimeException("Customer with id: " + customerId + " does not exist");
        customerRepository.delete(customerRepository.findByCustomerIdentifier_CustomerId(customerId));
    }
}
