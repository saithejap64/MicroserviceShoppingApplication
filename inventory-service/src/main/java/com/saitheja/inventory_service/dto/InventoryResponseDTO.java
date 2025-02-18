package com.saitheja.inventory_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryResponseDTO {
    private String skuCode;
    private boolean isInStock;
    private Integer quantity;
}
