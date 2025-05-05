package com.mosuuuutech.CRUD.API.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CheckConnection {
    @GetMapping("/health_check")
    public String HealthCheck(){
        return "Status 200 OK";
    }
}
