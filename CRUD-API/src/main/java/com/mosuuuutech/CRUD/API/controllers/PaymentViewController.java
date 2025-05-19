package com.mosuuuutech.CRUD.API.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/payment")
public class PaymentViewController {

    @GetMapping("/qr")
    public String showQRPaymentPage() {
        return "qr-payment";
    }
}