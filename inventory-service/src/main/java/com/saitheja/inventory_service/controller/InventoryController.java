package com.saitheja.inventory_service.controller;

import com.saitheja.inventory_service.dto.InventoryResponseDTO;
import com.saitheja.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    //http://localhost:8083/api/inventory/iphone_13, iphone_13_white
    //http://localhost:8083/api/inventory?skuCode=iphone_13&skuCode=iphone_13_white
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponseDTO> isInStock(@RequestParam List<String> skuCode) {
        return inventoryService.isInStock(skuCode);
    }
}
