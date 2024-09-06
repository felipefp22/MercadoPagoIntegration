package com.SharedCheksMercadoPagoIntegration.Controllers;


import com.SharedCheksMercadoPagoIntegration.Entities.Enums.KindOfSubscription;
import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.DTOs.PayerDTO;
import com.SharedCheksMercadoPagoIntegration.Servicies.OrdersService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/orders")
public class OrdersController {
    private final OrdersService ordersService;

    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    // <>-------------- Methods --------------<>
    @PostMapping("/create/{kindOfSubscription}")
    public Object create(@RequestBody PayerDTO payerDTO,
                        @PathVariable KindOfSubscription kindOfSubscription) {
        if(payerDTO.email() == null) throw new IllegalArgumentException("Email is required");

        return ordersService.createOrder(payerDTO,kindOfSubscription.getItemsDTO());
    }

}
