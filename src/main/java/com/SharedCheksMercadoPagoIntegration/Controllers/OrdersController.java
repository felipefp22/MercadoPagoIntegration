package com.SharedCheksMercadoPagoIntegration.Controllers;

import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.DTOs.ItemsAndPayerDTO;
import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.DTOs.ItemsDTO;
import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.DTOs.PayerDTO;
import com.SharedCheksMercadoPagoIntegration.Servicies.OrdersService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrdersController {
    private final OrdersService ordersService;

    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    // <>-------------- Methods --------------<>
    @PostMapping("/create-30days-order")
    public Object create30daysorder(@RequestBody PayerDTO payerDTO) {
        ItemsDTO itemsDTO = new ItemsDTO("Assinatura 30 dias", "BRL", 1, 9.99, "PREMIUM30");
        if(payerDTO.email() == null) throw new IllegalArgumentException("Email is required");

        return ordersService.createOrder(payerDTO, itemsDTO);
    }

    @PostMapping("/create-90days-order")
    public Object create90daysorder(@RequestBody PayerDTO payerDTO) {
        ItemsDTO itemsDTO = new ItemsDTO("Assinatura 90 dias", "BRL", 1, 26.99, "PREMIUM90");
        if(payerDTO.email() == null) throw new IllegalArgumentException("Email is required");

        return ordersService.createOrder(payerDTO, itemsDTO);
    }

    @PostMapping("/create-365days-order")
    public Object create365daysorder(@RequestBody PayerDTO payerDTO) {
        ItemsDTO itemsDTO = new ItemsDTO("Assinatura 365 dias", "BRL", 1, 99.99, "PREMIUM365");
        if(payerDTO.email() == null) throw new IllegalArgumentException("Email is required");

        return ordersService.createOrder(payerDTO, itemsDTO);
    }

}
