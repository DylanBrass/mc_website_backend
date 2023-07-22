package com.mc_website.customersservice.datamapperlayer;

import com.mc_website.customersservice.datalayer.Customer;
import com.mc_website.customersservice.presentationlayer.CustomerResponseModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerResponseMapper {
    @Mapping(expression = "java(customer.getCustomerIdentifier().getCustomerId())",  target = "customerId")
    CustomerResponseModel entityToResponseModel(Customer customer);

    List<CustomerResponseModel> entityListToResponseModelList(List<Customer> customers);
}