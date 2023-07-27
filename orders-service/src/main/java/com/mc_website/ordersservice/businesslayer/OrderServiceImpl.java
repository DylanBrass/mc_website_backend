package com.mc_website.ordersservice.businesslayer;

import com.mc_website.ordersservice.datalayer.CustomerIdentifier;
import com.mc_website.ordersservice.datalayer.OrdersRepository;
import com.mc_website.ordersservice.datamapperlayer.OrderRequestMapper;
import com.mc_website.ordersservice.datamapperlayer.OrderResponseMapper;
import com.mc_website.ordersservice.presentationlayer.OrderRequestModel;
import com.mc_website.ordersservice.presentationlayer.OrderResponseModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRequestMapper orderRequestMapper;
    private final OrderResponseMapper orderResponseMapper;
    private final OrdersRepository ordersRepository;

    @Override
    public OrderResponseModel addOrder(OrderRequestModel orderRequestModel) {
        return orderResponseMapper.entityToResponseModel(ordersRepository.insert(orderRequestMapper.requestModelToEntity(orderRequestModel,new CustomerIdentifier(orderRequestModel.getCustomer()))));
    }

    @Override
    public List<OrderResponseModel> getAllOrders() {
        return null;
    }
}
