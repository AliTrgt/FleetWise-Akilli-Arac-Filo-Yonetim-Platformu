package com.example.AuthService.dto.person.request;

import com.example.AuthService.entity.Role;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class InsertPerson {
    private String username;
    private String password;
    private int roleId;
    private String email;
    private String phoneNumber;
    private MultipartFile image;
}
