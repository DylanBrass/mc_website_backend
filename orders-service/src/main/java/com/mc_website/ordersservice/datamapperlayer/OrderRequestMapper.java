package com.mc_website.ordersservice.datamapperlayer;

import com.mc_website.ordersservice.datalayer.CustomerIdentifier;
import com.mc_website.ordersservice.datalayer.Orders;
import com.mc_website.ordersservice.presentationlayer.OrderRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.core.annotation.Order;

@Mapper(componentModel = "spring")
public interface OrderRequestMapper {
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "orderIdentifier", ignore = true),
            @Mapping(expression = "java(customerIdentifier)",target = "customer"),
    })
    Orders requestModelToEntity(OrderRequestModel orderRequestModel, CustomerIdentifier customerIdentifier);

}
