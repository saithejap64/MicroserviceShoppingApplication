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

import java.util.*;
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


        boolean allProductsInStock= Arrays.stream(inventoryResponseArray)
                .allMatch(InventoryResponseDTO::isInStock);


        //custom function to find unmatched product skuCodes
        Set<String > inventorySkuCodes = Arrays.stream(inventoryResponseArray).map(InventoryResponseDTO::getSkuCode)
                .collect(Collectors.toSet());

        List<String > skuCodesNonMatch= skuCodes.stream()
                .filter(sku -> !inventorySkuCodes.contains(sku))
                .collect(Collectors.toList());

        List<String > skuCodeOutOfQuantity= Arrays.stream(inventoryResponseArray)
                .filter((item ->  item.isInStock()==false))
                .map(InventoryResponseDTO::getSkuCode)
                .collect(Collectors.toList());

        //System.out.println(skuCodesNonMatch);


        if(allProductsInStock && skuCodesNonMatch.isEmpty()){
            orderRepository.save(order);
        }else if(!skuCodesNonMatch.isEmpty()){
            throw new ProductNotFoundException("Requested product " + skuCodesNonMatch +" is not found in the inventory, please try again later");
        }
        else {
            throw new ProductNotFoundException("Requested product, " +skuCodeOutOfQuantity+" is out of quantity,  please try again later");
        }


    }

    private OrderLineItems mapToDTO(OrderLineItemsDTO orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
