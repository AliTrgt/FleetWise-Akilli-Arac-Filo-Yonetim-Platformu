package com.example.DriverService.service;


import com.example.DriverService.dto.driver.request.InsertDriver;

public interface IDriverService {

    void createDriver(String token, InsertDriver driver);


}
