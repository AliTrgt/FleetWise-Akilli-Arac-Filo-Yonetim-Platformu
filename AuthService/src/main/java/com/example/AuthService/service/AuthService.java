package com.example.AuthService.service;

import com.example.AuthService.dto.auth.request.AuthRequest;
import com.example.AuthService.dto.person.request.InsertPerson;
import com.example.AuthService.dto.person.response.PersonViewModel;
import com.example.AuthService.entity.Person;
import com.example.AuthService.repository.PersonRepository;
import com.example.AuthService.security.JWTResponse;
import com.example.AuthService.security.JwtService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class AuthService {
    private static final Logger _logger = LoggerFactory.getLogger(AuthService.class);
    private final PersonRepository personRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public AuthService(PersonRepository personRepository, JwtService jwtService, AuthenticationManager authenticationManager, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public JWTResponse login(AuthRequest authRequest) throws Exception {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.username(), authRequest.password()));
        if (authentication.isAuthenticated()) {
            Person person = personRepository.findByUsername(authRequest.username());

            if (!BCrypt.checkpw(authRequest.password(), person.getPassword())) {
                _logger.warn("Incorrect password for user: {}", authRequest.username());
                throw new Exception("Kullanıcı adı ya da şifre hatalı!");
            }

            String token = jwtService.generateToken(person);
            String refreshToken = jwtService.generateRefreshToken(person);
            _logger.info("Login successful for user: {}", authRequest.username());
            return new JWTResponse(token, refreshToken);
        }

        throw new Exception("KULLANICI ADI YA DA SIFRE HATALI");

    }

    public JWTResponse refreshToken(String token) throws Exception {
        String username = jwtService.extractUser(token);
        Person person = personRepository.findByUsername(username);

        if (!jwtService.validateToken(token, person)) {
            _logger.warn("Token validation failed for user: {}", username);
            throw new Exception("Oturum süresi dolmuş!");
        }
        _logger.info("Token refreshed for user: {}", username);
        token = jwtService.generateToken(person);
        String refreshToken = jwtService.generateRefreshToken(person);
        return new JWTResponse(token, refreshToken);
    }

    public void register(InsertPerson insertPerson) {
        try {
            Person person = modelMapper.map(insertPerson, Person.class);
            personRepository.save(person);
            modelMapper.map(person, PersonViewModel.class);
            _logger.info("Registration successful for: {}", insertPerson.getUsername());
        } catch (Exception e) {
            _logger.error("Registration failed for: {} | Reason: {}", insertPerson.getUsername(), e.getMessage(), e);
            throw new NoSuchElementException(e);
        }
    }

    public void changePassword(String token, String newPassword) {
        try {
            String username = jwtService.extractUser(token);
            Person person = personRepository.findByUsername(username);
            if (person == null) {
                throw new RuntimeException("Kullanıcı bulunamadı");
            }
            person.setPassword(passwordEncoder.encode(newPassword));
            personRepository.save(person);
            _logger.info("Password successfully changed for user: {}", username);
        } catch (Exception e) {
            _logger.error("Error while changing password: {}", e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }


}
