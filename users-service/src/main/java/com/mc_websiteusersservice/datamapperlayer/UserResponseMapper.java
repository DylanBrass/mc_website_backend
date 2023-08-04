package com.mc_websiteusersservice.datamapperlayer;

import com.mc_websiteusersservice.datalayer.User;
import com.mc_websiteusersservice.presentationlayer.UserResponseModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface UserResponseMapper {
    @Mapping(expression = "java(user.getUserIdentifier().getUserId())",  target = "userId")
    UserResponseModel entityToResponseModel(User user);

    List<UserResponseModel> entityListToResponseModelList(List<User> users);
}
