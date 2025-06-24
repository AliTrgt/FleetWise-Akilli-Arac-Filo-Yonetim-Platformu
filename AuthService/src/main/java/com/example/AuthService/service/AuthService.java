package com.example.AuthService.service;

import com.example.AuthService.dto.auth.request.AuthRequest;
import com.example.AuthService.dto.person.request.InsertPerson;
import com.example.AuthService.dto.person.response.PersonViewModel;
import com.example.AuthService.entity.Person;
import com.example.AuthService.repository.PersonRepository;
import com.example.AuthService.security.JWTResponse;
import com.example.AuthService.security.JwtService;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class AuthService {

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
                throw new Exception("Kullanıcı adı ya da şifre hatalı!");
            }

            String token = jwtService.generateToken(person);
            String refreshToken = jwtService.generateRefreshToken(person);
            return new JWTResponse(token, refreshToken);
        }

        throw new Exception("KULLANICI ADI YA DA SIFRE HATALI");

    }

    public JWTResponse refreshToken(String token) throws Exception{
            String username = jwtService.extractUser(token);
            Person person = personRepository.findByUsername(username);

            if (!jwtService.validateToken(token,person)){
                throw new Exception("Oturum süresi dolmuş!");
            }

            token = jwtService.generateToken(person);
            String refreshToken = jwtService.generateRefreshToken(person);
            return new JWTResponse(token,refreshToken);
    }

    public void register(InsertPerson insertPerson){
        try {
            Person person = modelMapper.map(insertPerson,Person.class);
            personRepository.save(person);
            modelMapper.map(person,PersonViewModel.class);
        }catch (Exception e){
            throw new NoSuchElementException(e);
        }
    }
    public void changePassword(String token,String newPassword){
        try {
            String username = jwtService.extractUser(token);
            Person person = personRepository.findByUsername(username);
            if(person == null){
                    throw new RuntimeException("Kullanıcı bulunamadı");
            }
            person.setPassword(passwordEncoder.encode(newPassword));
            personRepository.save(person);

        }catch (Exception e){
                throw new RuntimeException(e.getMessage());
        }
    }



}
