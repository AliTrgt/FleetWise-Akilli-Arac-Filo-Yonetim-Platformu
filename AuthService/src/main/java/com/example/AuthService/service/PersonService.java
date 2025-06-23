package com.example.AuthService.service;

import com.example.AuthService.dto.person.request.InsertPerson;
import com.example.AuthService.dto.person.request.UpdatePerson;
import com.example.AuthService.dto.person.response.PersonViewModel;
import com.example.AuthService.entity.Person;
import com.example.AuthService.repository.PersonRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final ModelMapper modelMapper;

    public PersonService(PersonRepository personRepository, ModelMapper modelMapper) {
        this.personRepository = personRepository;
        this.modelMapper = modelMapper;
    }

    public List<PersonViewModel> getAll() {
        return personRepository.findAll()
                .stream()
                .map(person -> modelMapper.map(person, PersonViewModel.class))
                .collect(Collectors.toList());
    }

    public PersonViewModel updateUser(UpdatePerson updatePerson){
            Person person = modelMapper.map(updatePerson,Person.class);
            personRepository.save(person);
            return modelMapper.map(person, PersonViewModel.class);
    }

    public void delete(int id){
            personRepository.deleteById(id);
    }

}
