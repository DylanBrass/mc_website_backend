package com.mc_website.ordersservice.businesslayer;

import com.mc_website.ordersservice.datalayer.*;
import com.mc_website.ordersservice.datamapperlayer.OrderRequestMapper;
import com.mc_website.ordersservice.datamapperlayer.OrderResponseMapper;
import com.mc_website.ordersservice.presentationlayer.OrderRequestModel;
import com.mc_website.ordersservice.presentationlayer.OrderResponseModel;
import com.mc_website.ordersservice.utils.exceptions.InvalidInputException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRequestMapper orderRequestMapper;
    private final OrderResponseMapper orderResponseMapper;
    private final OrdersRepository ordersRepository;

    @Override
    public OrderResponseModel addOrder(OrderRequestModel orderRequestModel, String customerId) {

        orderRequestModel.getItems().forEach(item -> {
            if (!findByOrderType(item.getOrderType().toString())) {
                throw new InvalidInputException("Order type entered is not a valid type : " + item.getOrderType().toString());
            }
            if (!findByItemType(item.getItemType().toString())) {
                throw new InvalidInputException("Item type entered is not a valid type : " + item.getItemType().toString());
            }
        });


        Orders savedOrder = orderRequestMapper.requestModelToEntity(orderRequestModel,new CustomerIdentifier(customerId));
        savedOrder.setOrderIdentifier(new OrderIdentifier());
        List<Item> items = new ArrayList<>();
        items.addAll(orderRequestModel.getItems());
        savedOrder.setItems(items);


        return orderResponseMapper.entityToResponseModel(ordersRepository.insert(savedOrder));
    }

    @Override
    public List<OrderResponseModel> getAllOrders() {
        return orderResponseMapper.entitiesResponseModel(ordersRepository.findAll());
    }

    @Override
    public List<OrderResponseModel> getAllOrdersForCustomer(String customerId) {

        return orderResponseMapper.entitiesResponseModel(ordersRepository.getOrdersByCustomer_CustomerId(customerId));
    }

    public static Boolean findByOrderType(String typeStr) {
        boolean found = false;
        for (OrderType type : OrderType.values()) {
            if (type.name().equalsIgnoreCase(typeStr)) {
                found = true;
                break;
            }
        }
        return found;
    }
    public static Boolean findByItemType(String typeStr) {
        boolean found = false;
        for (ItemType type : ItemType.values()) {
            if (type.name().equalsIgnoreCase(typeStr)) {
                found = true;
                break;
            }
        }
        return found;
    }
}
