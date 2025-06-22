package com.example.AuthService.dto.person.response;

import com.example.AuthService.entity.Role;
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
