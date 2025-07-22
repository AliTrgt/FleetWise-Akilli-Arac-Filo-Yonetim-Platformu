package com.example.AuthService.controller;


import com.example.AuthService.dto.person.request.InsertPerson;
import com.example.AuthService.dto.person.request.UpdatePerson;
import com.example.AuthService.dto.person.response.PersonViewModel;
import com.example.AuthService.service.PersonService;
import org.springframework.cloud.commons.security.ResourceServerTokenRelayAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/person")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public ResponseEntity<List<PersonViewModel>> getAll() {
        List<PersonViewModel> personViewModels = personService.getAll();
        return ResponseEntity.ok(personViewModels);
    }

    @PutMapping
    public ResponseEntity<?> updateUser(
            @ModelAttribute UpdatePerson updatePerson) {

        try {
            PersonViewModel personViewModel = personService.updateUser(updatePerson);
            return ResponseEntity.ok(personViewModel);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable int id){
             var person = personService.findById(id);
             return ResponseEntity.ok(person);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        personService.delete(id);
        return ResponseEntity.ok().build();
    }

}
