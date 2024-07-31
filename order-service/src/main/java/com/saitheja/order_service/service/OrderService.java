package com.saitheja.order_service.service;

import com.saitheja.order_service.dto.InventoryResponseDTO;
import com.saitheja.order_service.dto.OrderLineItemsDTO;
import com.saitheja.order_service.dto.OrderRequestDTO;
import com.saitheja.order_service.model.Order;
import com.saitheja.order_service.model.OrderLineItems;
import com.saitheja.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient webClient;

    public void placeOrder(OrderRequestDTO orderRequestDTO){
        Order order=new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems=
                orderRequestDTO.getOrderLineItemsDTOS()
                .stream()
                .map(this::mapToDTO)
                .toList();

        order.setOrderLineItems(orderLineItems);
        orderRepository.save(order);
    }

    private OrderLineItems mapToDTO(OrderLineItemsDTO orderLineItemsDTO) {
        OrderLineItems orderLineItems=new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDTO.getPrice());
        orderLineItems.setQuantity(orderLineItemsDTO.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDTO.getSkuCode());
        return orderLineItems;
    }
}
