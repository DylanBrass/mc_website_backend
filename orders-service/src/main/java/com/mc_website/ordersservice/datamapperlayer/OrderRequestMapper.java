package com.mc_website.ordersservice.datamapperlayer;

import com.mc_website.ordersservice.datalayer.Orders;
import com.mc_website.ordersservice.presentationlayer.OrderRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface OrderRequestMapper {
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "orderIdentifier", ignore = true),
            @Mapping(target = "user",ignore = true)
    })
    Orders requestModelToEntity(OrderRequestModel orderRequestModel);

}
