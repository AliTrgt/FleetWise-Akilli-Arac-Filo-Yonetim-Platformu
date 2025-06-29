package com.example.DriverService.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/driver")
public class TestController {

    @GetMapping
    public String hello(){
            return "SELAMLAR NASILSIN";
    }


}
