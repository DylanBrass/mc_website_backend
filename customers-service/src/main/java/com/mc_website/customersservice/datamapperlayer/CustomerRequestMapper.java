package com.mc_website.customersservice.datamapperlayer;

import com.mc_website.customersservice.datalayer.Customer;
import com.mc_website.customersservice.presentationlayer.CustomerRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerRequestMapper {
    @Mapping(target = "customerIdentifier", ignore = true)
    @Mapping(target = "id", ignore = true)
    Customer requestModelToEntity(CustomerRequestModel customerRequestModel);

}
