package com.mc_website.ordersservice.datalayer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.mapstruct.EnumMapping;

import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
public class Item {
    String item;
    ItemType itemType;
    String description;
    OrderType orderType;
    Integer quantity;


}
