package com.mc_website.ordersservice.presentationlayer;

import com.mc_website.ordersservice.businesslayer.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.List;

@RestController
@RequestMapping("api/v1/users")
public class OrderController {
    OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponseModel>> getAllOrders(){
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PostMapping("/{userId}/orders")
    public ResponseEntity<OrderResponseModel> addOrder(@RequestBody OrderRequestModel orderRequestModel,@PathVariable String userId) throws MessagingException {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.addOrder(orderRequestModel, userId));
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<OrderResponseModel>> getAllOrdersForUser(@PathVariable String userId){
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
    public ResponseEntity<OrderResponseModel> updateOrder(@RequestBody OrderRequestModel orderRequestModel, @PathVariable String orderId) throws MessagingException {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.updateOrder(orderRequestModel, orderId));

    }

}