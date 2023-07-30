package com.mc_website.apigateway.presentation.Orders;

import com.mc_website.apigateway.businesslayer.Orders.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/customers")
public class OrderController {
    OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public ResponseEntity<OrderResponseModel[]> getAllOrders(){
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PostMapping("/{customerId}/orders")
    public ResponseEntity<OrderResponseModel> addOrder(@RequestBody OrderRequestModel orderRequestModel, @PathVariable String customerId){
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.addOrder(orderRequestModel,customerId));
    }

    @GetMapping("/{customerId}/orders")
    public ResponseEntity<OrderResponseModel[]> getAllOrdersForCustomer(@PathVariable String customerId){
        return ResponseEntity.ok(orderService.getAllOrdersForCustomer(customerId));
    }

}
