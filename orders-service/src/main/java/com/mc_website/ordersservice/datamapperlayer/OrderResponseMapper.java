package com.mc_website.ordersservice.datamapperlayer;

import com.mc_website.ordersservice.datalayer.Orders;
import com.mc_website.ordersservice.presentationlayer.OrderResponseModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface OrderResponseMapper {

    @Mapping(expression="java(order.getOrderIdentifier().getOrderId())", target = "orderId")
    @Mapping(expression="java(order.getUser().getUserId())", target = "user")
    OrderResponseModel entityToResponseModel(Orders order);

    @Mapping(expression="java(order.getItems())", target = "items")
    List<OrderResponseModel> entitiesResponseModel(List<Orders> orders);
}
