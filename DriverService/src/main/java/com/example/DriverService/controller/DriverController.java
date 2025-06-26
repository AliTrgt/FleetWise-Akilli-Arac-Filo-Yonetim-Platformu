package com.example.DriverService.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/driver")
public class DriverController {

    @GetMapping
    public String hello(){
        System.out.println("Selamlar istek atıldı");
        return "Merhabalar Nasılsınız";
    }


}
