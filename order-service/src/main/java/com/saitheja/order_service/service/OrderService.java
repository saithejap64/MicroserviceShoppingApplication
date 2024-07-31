package com.saitheja.order_service.service;

import com.saitheja.order_service.dto.InventoryResponseDTO;
import com.saitheja.order_service.dto.OrderLineItemsDTO;
import com.saitheja.order_service.dto.OrderRequestDTO;
import com.saitheja.order_service.exception.ProductNotFoundException;
import com.saitheja.order_service.model.Order;
import com.saitheja.order_service.model.OrderLineItems;
import com.saitheja.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    public void placeOrder(OrderRequestDTO orderRequestDTO){
        Order order=new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems=
                orderRequestDTO.getOrderLineItemsDTOS()
                .stream()
                .map(this::mapToDTO)
                .toList();

        order.setOrderLineItems(orderLineItems);


        List<String> skuCodes = order.getOrderLineItems().stream().
                map(OrderLineItems::getSkuCode).toList();

        //call inventory service, and place order if product is in stock
        InventoryResponseDTO[] inventoryResponseArray=webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode",skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponseDTO[].class)
                .block();


        Boolean allProductsInStock= Arrays.stream(inventoryResponseArray)
                .allMatch(InventoryResponseDTO::isInStock);
        if(allProductsInStock){
            orderRepository.save(order);
        }else {
            throw new ProductNotFoundException("Requested product is/are not found, please try again later");
        }

//        List<InventoryResponseDTO> allProductsInNotInStock= Arrays.stream(inventoryResponseArray)
//                .filter(x -> skuCodes.contains(x.getSkuCode()) && !x.isInStock()).collect(Collectors.toList());

//        if(allProductsInNotInStock.isEmpty()){
//            orderRepository.save(order);
//        }else {
//            throw new ProductNotFoundException("Following items are not present"+allProductsInNotInStock);
//        }


    }

    private OrderLineItems mapToDTO(OrderLineItemsDTO orderLineItemsDTO) {
        OrderLineItems orderLineItems=new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDTO.getPrice());
        orderLineItems.setQuantity(orderLineItemsDTO.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDTO.getSkuCode());
        return orderLineItems;
    }
}
