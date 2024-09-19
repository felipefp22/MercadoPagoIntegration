package com.SharedCheksMercadoPagoIntegration.Controllers;


import com.SharedCheksMercadoPagoIntegration.Entities.Enums.KindOfSubscription;
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
    @PostMapping("/create/{kindOfSubscription}")
    public PreferenceRetunDTO create(@RequestBody PayerDTO payerDTO,
                                     @PathVariable KindOfSubscription kindOfSubscription) {
        if (payerDTO.email() == null) throw new IllegalArgumentException("Email is required");

        return ordersService.createOrder(payerDTO, kindOfSubscription.getItemsDTO());
    }

    @PutMapping("/cancel-and-create-new-order/{kindOfSubscription}")
    public PreferenceRetunDTO cancelAndCreateNewOrder(@RequestBody PayerDTO payerDTO,
                                                      @PathVariable KindOfSubscription kindOfSubscription) {
        if (payerDTO.email() == null) throw new IllegalArgumentException("Email is required");
        SubscribeOrderPendind subscribeOrderPendindFound =
                subscriptionPendingRepo.findByEmailProfileID(payerDTO.email()).stream().findFirst().orElse(null);

        ordersService.cancelAndExpireNoFurtherOrder(subscribeOrderPendindFound);

        return ordersService.createOrder(payerDTO, kindOfSubscription.getItemsDTO());
    }

}
