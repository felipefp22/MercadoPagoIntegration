package com.SharedCheksMercadoPagoIntegration;

import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.DTOs.ItemsAndPayerDTO;
import com.SharedCheksMercadoPagoIntegration.Servicies.OrdersService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrdersController {
    private final OrdersService ordersService;

    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    // <>-------------- Methods --------------<>
    @PostMapping("/create")
    public Object createOrder(@RequestBody ItemsAndPayerDTO itemsAndPayerDTO) {

        return ordersService.createOrder(itemsAndPayerDTO);
    }
}
