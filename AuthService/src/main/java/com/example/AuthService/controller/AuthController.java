package com.example.AuthService.controller;


import com.example.AuthService.dto.auth.request.AuthRequest;
import com.example.AuthService.dto.person.request.InsertPerson;
import com.example.AuthService.dto.person.response.PersonViewModel;
import com.example.AuthService.entity.Person;
import com.example.AuthService.repository.PersonRepository;
import com.example.AuthService.security.JWTResponse;
import com.example.AuthService.security.JwtService;
import com.example.AuthService.security.SecurityService;
import com.example.AuthService.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final PersonRepository personRepository;
    private final SecurityService securityService;

    public AuthController(AuthService authService, JwtService jwtService, PersonRepository personRepository, SecurityService securityService) {
        this.authService = authService;
        this.jwtService = jwtService;
        this.personRepository = personRepository;
        this.securityService = securityService;
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String autHeader) {
        try {
            String token = autHeader.substring(7);
            String username = jwtService.extractUser(token);
            Person person = personRepository.findByUsername(username);
            return ResponseEntity.ok(person);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestParam String token) throws Exception {
        try {
            JWTResponse response = authService.refreshToken(token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) throws Exception {
        try {
            JWTResponse response = authService.login(authRequest);

            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }


    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> createUser(
            @ModelAttribute InsertPerson insertPerson
    ) {
        try {
            authService.register(insertPerson);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestHeader("Authorization") String authHeader, @RequestParam String newPassword) throws Exception {
        String token = authHeader.substring(7);
        authService.changePassword(token, newPassword);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validate(@RequestHeader("Authorization") String authHeader) throws Exception {
        try {
            String token = authHeader.substring(7);
            String username = jwtService.extractUser(token);
            UserDetails userDetails = securityService.loadUserByUsername(username);

            boolean isValid = jwtService.validateToken(token, userDetails);

            if (!isValid) {
                return ResponseEntity.status(401).body("Token geçersiz");
            }
            return ResponseEntity.ok(isValid);

        } catch (Exception e) {
            return ResponseEntity.status(401).body("Token validation error: " + e.getMessage());
        }

    }
}
