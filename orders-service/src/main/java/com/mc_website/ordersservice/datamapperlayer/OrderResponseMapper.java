package com.mc_website.ordersservice.datamapperlayer;

import com.mc_website.ordersservice.datalayer.Orders;
import com.mc_website.ordersservice.presentationlayer.Customer.CustomerResponseModel;
import com.mc_website.ordersservice.presentationlayer.OrderResponseModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderResponseMapper {

    @Mapping(expression="java(order.getOrderIdentifier().getOrderId())", target = "orderId")
    @Mapping(expression="java(order.getItems())", target = "items")
    @Mapping(expression="java(customerResponseModel)", target = "customer")
    OrderResponseModel entityToResponseModel(Orders order, CustomerResponseModel customerResponseModel);

    List<OrderResponseModel> customerResponseModel(List<Orders> orders);
}