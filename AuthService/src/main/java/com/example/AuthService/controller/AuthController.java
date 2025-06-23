package com.example.AuthService.controller;


import com.example.AuthService.dto.auth.request.AuthRequest;
import com.example.AuthService.dto.person.request.InsertPerson;
import com.example.AuthService.dto.person.response.PersonViewModel;
import com.example.AuthService.entity.Person;
import com.example.AuthService.repository.PersonRepository;
import com.example.AuthService.security.JWTResponse;
import com.example.AuthService.security.JwtService;
import com.example.AuthService.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final PersonRepository personRepository;

    public AuthController(AuthService authService, JwtService jwtService, PersonRepository personRepository) {
        this.authService = authService;
        this.jwtService = jwtService;
        this.personRepository = personRepository;
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
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
