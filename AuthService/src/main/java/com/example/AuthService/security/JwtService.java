package com.example.AuthService.security;

import aj.org.objectweb.asm.ClassTooLargeException;
import com.example.AuthService.entity.Person;
import com.example.AuthService.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class JwtService {

    @Value("${jwt.key}")
    private String SECRET_KEY;
    private final long accessTokenExpiration = TimeUnit.DAYS.toMillis(3);
    private final long refreshTokenExpiration = TimeUnit.DAYS.toMillis(7);


    public String generateToken(UserDetails user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", ((Person) user).getId());
        claims.put("role", user.getAuthorities().iterator().next().getAuthority());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(generateKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(UserDetails user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(generateKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(generateKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token,UserDetails userDetails){
            String username = extractUser(token);
            Date expirationDate = extractDate(token);
            return username.equals(userDetails.getUsername()) && !expirationDate.before(new Date());
    }


    public String extractUser(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }

    public Date extractDate(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getExpiration();
    }

    public Integer extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("userId", Integer.class);
    }

    public String extractRole(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("role", String.class);
    }

    public Key generateKey() {
        byte[] hashKey = Decoders.BASE64URL.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(hashKey);
    }

}
