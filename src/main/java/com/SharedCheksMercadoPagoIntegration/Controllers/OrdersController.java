package com.SharedCheksMercadoPagoIntegration.Controllers;


import com.SharedCheksMercadoPagoIntegration.Entities.Enums.KindOfPremium;
import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.Preference.PreferenceDTOS.PayerDTO;
import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.Preference.PreferenceDTOS.PreferenceRetunDTO;
import com.SharedCheksMercadoPagoIntegration.Entities.PremiumOrderPendind;
import com.SharedCheksMercadoPagoIntegration.Repositories.PremiumPendingRepo;
import com.SharedCheksMercadoPagoIntegration.Servicies.OrdersService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/orders")
public class OrdersController {
    private final OrdersService ordersService;
    private final PremiumPendingRepo premiumPendingRepo;

    public OrdersController(OrdersService ordersService, PremiumPendingRepo premiumPendingRepo) {
        this.ordersService = ordersService;
        this.premiumPendingRepo = premiumPendingRepo;
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
        PremiumOrderPendind premiumOrderPendindFound =
                premiumPendingRepo.findByEmailProfileID(payerDTO.email()).stream().findFirst().orElse(null);

        ordersService.cancelAndExpireNoFurtherOrder(premiumOrderPendindFound);

        return ordersService.createOrder(payerDTO, kindOfPremium.getItemsDTO());
    }

}
