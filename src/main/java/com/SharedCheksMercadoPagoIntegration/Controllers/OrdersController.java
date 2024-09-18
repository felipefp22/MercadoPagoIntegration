package com.SharedCheksMercadoPagoIntegration.Controllers;


import com.SharedCheksMercadoPagoIntegration.Entities.Enums.KindOfSubscription;
import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.Preference.PreferenceDTOS.PayerDTO;
import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.Preference.PreferenceDTOS.PreferenceRetunDTO;
import com.SharedCheksMercadoPagoIntegration.Entities.SubscribeOrderPendind;
import com.SharedCheksMercadoPagoIntegration.Repositories.SubscriptionPendentRepo;
import com.SharedCheksMercadoPagoIntegration.Servicies.OrdersService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/orders")
public class OrdersController {
    private final OrdersService ordersService;
    private final SubscriptionPendentRepo subscriptionPendentRepo;

    public OrdersController(OrdersService ordersService, SubscriptionPendentRepo subscriptionPendentRepo) {
        this.ordersService = ordersService;
        this.subscriptionPendentRepo = subscriptionPendentRepo;
    }

    // <>-------------- Methods --------------<>
    @PostMapping("/create/{kindOfSubscription}")
    public PreferenceRetunDTO create(@RequestBody PayerDTO payerDTO,
                                     @PathVariable KindOfSubscription kindOfSubscription) {
        if (payerDTO.email() == null) throw new IllegalArgumentException("Email is required");

        return ordersService.createOrder(payerDTO, kindOfSubscription.getItemsDTO());
    }

    @PostMapping("/create/{kindOfSubscription}")
    public PreferenceRetunDTO cancelAndCreateNewOrder(@RequestBody PayerDTO payerDTO,
                                                      @PathVariable KindOfSubscription kindOfSubscription) {
        if (payerDTO.email() == null) throw new IllegalArgumentException("Email is required");
        SubscribeOrderPendind subscribeOrderPendindFound =
                subscriptionPendentRepo.findByEmailProfileID(payerDTO.email()).stream().findFirst().orElse(null);

        ordersService.cancelAndExpireNoFurtherOrder(subscribeOrderPendindFound);

        return ordersService.createOrder(payerDTO, kindOfSubscription.getItemsDTO());
    }

}
