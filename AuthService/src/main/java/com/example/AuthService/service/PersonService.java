package com.example.AuthService.service;

import com.example.AuthService.dto.person.request.InsertPerson;
import com.example.AuthService.dto.person.request.UpdatePerson;
import com.example.AuthService.dto.person.response.PersonViewModel;
import com.example.AuthService.entity.Person;
import com.example.AuthService.repository.PersonRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class PersonService {
    private static final Logger _logger = LoggerFactory.getLogger(PersonService.class);
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

    public PersonViewModel updateUser(UpdatePerson updatePerson) {
        _logger.info("Updating person with id: {}", updatePerson.getId());
        Person person = modelMapper.map(updatePerson, Person.class);
        personRepository.save(person);
        _logger.debug("Updated person: {}", person);
        return modelMapper.map(person, PersonViewModel.class);
    }

    public void delete(int id) {
        _logger.warn("Attempted to delete non-existing person with id: {}", id);
        personRepository.deleteById(id);
        _logger.info("Deleted person with id: {}", id);
    }

}
