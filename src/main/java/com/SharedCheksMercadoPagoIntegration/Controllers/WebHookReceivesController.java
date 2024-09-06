package com.SharedCheksMercadoPagoIntegration.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webhook-receives")
public class WebHookReceivesController {

    @PostMapping("/mp-payments")
    public ResponseEntity receiveMpPayments(@RequestBody Object object) {

        System.out.println(object.toString());


        return ResponseEntity.ok().build();
    }
}
