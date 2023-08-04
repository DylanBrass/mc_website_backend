package com.mc_websiteusersservice.datamapperlayer;

import com.mc_websiteusersservice.datalayer.User;
import com.mc_websiteusersservice.presentationlayer.UserRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface UserRequestMapper {
    @Mapping(target = "userIdentifier", ignore = true)
    @Mapping(target = "id", ignore = true)
    User requestModelToEntity(UserRequestModel userRequestModel);

}
