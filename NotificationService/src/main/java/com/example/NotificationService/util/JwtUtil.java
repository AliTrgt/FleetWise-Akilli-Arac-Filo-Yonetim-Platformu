package com.example.NotificationService.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtUtil {

    @Value("${jwt.key}")
    private String key;

    public Claims extractAll(String token){
            return Jwts.parserBuilder()
                    .setSigningKey(getDecoders())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

    }

    public Integer extractUserId(String token){
            return extractAll(token).get("userId",Integer.class);
    }

    public Key getDecoders(){
        byte[] keyBytes = Decoders.BASE64.decode(key);
        return Keys.hmacShaKeyFor(keyBytes);
    }


}
