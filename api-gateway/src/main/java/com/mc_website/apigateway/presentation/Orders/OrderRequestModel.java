package com.mc_website.apigateway.presentation.Orders;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderRequestModel {

    @NotNull
    List<Item> items;
    @NotBlank
    String message;
}
