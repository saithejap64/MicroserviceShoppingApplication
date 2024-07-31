package com.saitheja.order_service.dto;

import com.saitheja.order_service.model.OrderLineItems;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDTO {
    private List<OrderLineItemsDTO> orderLineItemsDTOS;
}
