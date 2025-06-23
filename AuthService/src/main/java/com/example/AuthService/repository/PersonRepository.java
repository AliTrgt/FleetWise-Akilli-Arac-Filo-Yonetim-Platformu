package com.example.AuthService.repository;

import com.example.AuthService.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository  extends JpaRepository<Person,Integer> {
    Person findByUsername(String username);
}
