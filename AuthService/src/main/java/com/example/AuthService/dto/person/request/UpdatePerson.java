package com.example.AuthService.dto.person.request;

import com.example.AuthService.entity.Role;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdatePerson {
    private int id;
    private String username;
    private int roleId;
    private String email;
    private String phoneNumber;
    private MultipartFile image;
}
