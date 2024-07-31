package com.saitheja.order_service.controller;

import com.saitheja.order_service.dto.OrderRequestDTO;
import com.saitheja.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(@RequestBody OrderRequestDTO orderRequestDTO){
        orderService.placeOrder(orderRequestDTO);
        return "Order Placed Successfully";
    }
}
