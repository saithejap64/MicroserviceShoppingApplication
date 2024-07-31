package com.saitheja.inventory_service.controller;

import com.saitheja.inventory_service.dto.InventoryResponseDTO;
import com.saitheja.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;


    // http://localhost:8083/api/inventory/iphone_13,iphone13_white

    // http://localhost:8083/api/inventory?skuCode=iphone_13&skuCode=iphone13_white
    @GetMapping("/{sku-code}")
    @ResponseStatus(HttpStatus.OK)
    public boolean isInStock(@PathVariable("sku-code") String skuCode){
        return inventoryService.isInStock(skuCode);
    }
}
