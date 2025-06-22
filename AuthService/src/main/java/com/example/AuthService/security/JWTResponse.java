package com.example.AuthService.security;

import lombok.Data;

@Data
public class JWTResponse {

    private String accessToken;
    private String refreshToken;


    public JWTResponse(String accessToken,String refreshToken){
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
    }



}
