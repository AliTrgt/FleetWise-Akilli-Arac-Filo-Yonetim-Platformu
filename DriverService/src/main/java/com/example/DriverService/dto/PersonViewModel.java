package com.example.DriverService.dto;

import lombok.Data;

import java.util.Date;

@Data
public class PersonViewModel {
    private int id;
    private String username;
    private int roleId;
    private Date createdAt;
    private String email;
    private String phoneNumber;
    private String image;
}
