package com.mc_website.apigateway.presentation.Orders;

import com.mc_website.apigateway.businesslayer.Orders.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/users")
@CrossOrigin(origins = "http://localhost:3000")
public class OrderController {
    OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public ResponseEntity<OrderResponseModel[]> getAllOrders(){
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PostMapping("/{userId}/orders")
    public ResponseEntity<OrderResponseModel> addOrder(@RequestBody OrderRequestModel orderRequestModel, @PathVariable String userId){
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.addOrder(orderRequestModel,userId));
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<OrderResponseModel[]> getAllOrdersForUser(@PathVariable String userId){
        return ResponseEntity.ok(orderService.getAllOrdersForUser(userId));
    }
    @DeleteMapping("/orders/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable String orderId){
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderResponseModel> getOrderById(@PathVariable String orderId){
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    @PutMapping("/orders/{orderId}")
    public ResponseEntity<OrderResponseModel> updateOrder(@RequestBody OrderRequestModel orderRequestModel, @PathVariable String orderId) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.updateOrder(orderRequestModel, orderId));

    }

}
