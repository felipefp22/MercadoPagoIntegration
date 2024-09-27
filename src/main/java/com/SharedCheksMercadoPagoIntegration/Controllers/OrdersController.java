package com.SharedCheksMercadoPagoIntegration.Controllers;


import com.SharedCheksMercadoPagoIntegration.Entities.Enums.KindOfPremium;
import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.Preference.PreferenceDTOS.PayerDTO;
import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.Preference.PreferenceDTOS.PreferenceRetunDTO;
import com.SharedCheksMercadoPagoIntegration.Entities.SubscribeOrderPendind;
import com.SharedCheksMercadoPagoIntegration.Repositories.SubscriptionPendingRepo;
import com.SharedCheksMercadoPagoIntegration.Servicies.OrdersService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/orders")
public class OrdersController {
    private final OrdersService ordersService;
    private final SubscriptionPendingRepo subscriptionPendingRepo;

    public OrdersController(OrdersService ordersService, SubscriptionPendingRepo subscriptionPendingRepo) {
        this.ordersService = ordersService;
        this.subscriptionPendingRepo = subscriptionPendingRepo;
    }

    // <>-------------- Methods --------------<>
    @PostMapping("/create/{kindOfPremium}")
    public PreferenceRetunDTO create(@RequestBody PayerDTO payerDTO,
                                     @PathVariable KindOfPremium kindOfPremium) {
        if (payerDTO.email() == null) throw new IllegalArgumentException("Email is required");

        return ordersService.createOrder(payerDTO, kindOfPremium.getItemsDTO());
    }

    @PutMapping("/cancel-and-create-new-order/{kindOfPremium}")
    public PreferenceRetunDTO cancelAndCreateNewOrder(@RequestBody PayerDTO payerDTO,
                                                      @PathVariable KindOfPremium kindOfPremium) {
        if (payerDTO.email() == null) throw new IllegalArgumentException("Email is required");
        SubscribeOrderPendind subscribeOrderPendindFound =
                subscriptionPendingRepo.findByEmailProfileID(payerDTO.email()).stream().findFirst().orElse(null);

        ordersService.cancelAndExpireNoFurtherOrder(subscribeOrderPendindFound);

        return ordersService.createOrder(payerDTO, kindOfPremium.getItemsDTO());
    }

}
