package com.mc_website.apigateway.datamapperlayer;

import com.mc_website.apigateway.presentation.User.UserResponseModel;
import com.mc_website.apigateway.presentation.User.UserResponseModelPasswordLess;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface UserResponseModelLessPasswordMapper {

    UserResponseModelPasswordLess responseModelToResponseModelLessPassword(UserResponseModel userResponseModel);
    UserResponseModelPasswordLess[] responseModelsToResponseModelsLessPassword(UserResponseModel[] userResponseModels);

}
