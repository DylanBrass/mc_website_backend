package com.mc_website.apigateway.presentation;

import com.mc_website.apigateway.businesslayer.CustomersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/customers")
@CrossOrigin(origins = "http://localhost:3000")
public class CustomerController {
    CustomersService customerService;
    public CustomerController(CustomersService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<CustomerResponseModel[]> getCustomers(){
        return ResponseEntity.ok(customerService.getCustomers());
    }
    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerResponseModel> getCustomerById(@PathVariable String customerId){
        return ResponseEntity.ok(customerService.getCustomerById(customerId));
    }
    @PostMapping()
    public ResponseEntity<CustomerResponseModel> addCustomer(@RequestBody CustomerRequestModel customerRequestModel){
        return ResponseEntity.ok(customerService.addCustomer(customerRequestModel));
    }
    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerResponseModel> updateCustomer(@PathVariable String customerId, @RequestBody CustomerRequestModel customerRequestModel){
        return ResponseEntity.ok(customerService.updateCustomer(customerId, customerRequestModel));
    }
    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable String customerId){
        customerService.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }
}
