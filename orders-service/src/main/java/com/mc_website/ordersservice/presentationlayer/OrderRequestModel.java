package com.mc_website.ordersservice.presentationlayer;

import com.mc_website.ordersservice.datalayer.Item;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderRequestModel {

    @NotNull
    List<Item> items;
    @NotBlank
    String message;
}
