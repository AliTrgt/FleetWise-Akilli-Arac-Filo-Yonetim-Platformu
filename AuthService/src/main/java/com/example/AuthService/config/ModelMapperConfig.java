package com.example.AuthService.config;

import com.example.AuthService.dto.person.request.InsertPerson;
import com.example.AuthService.dto.person.request.UpdatePerson;
import com.example.AuthService.dto.person.response.PersonViewModel;
import com.example.AuthService.entity.Person;
import com.example.AuthService.entity.Role;
import com.example.AuthService.repository.PersonRepository;
import com.example.AuthService.repository.RoleRepository;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.NoSuchElementException;

@Configuration
public class ModelMapperConfig {

    private final PasswordEncoder passwordEncoder;
    private final PersonRepository personRepository;
    private final RoleRepository roleRepository;
    public ModelMapperConfig(PasswordEncoder passwordEncoder, PersonRepository personRepository, RoleRepository roleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.personRepository = personRepository;
        this.roleRepository = roleRepository;
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);


        Converter<InsertPerson, Person> insertPersonPersonConverter = new Converter<InsertPerson, Person>() {
            @Override
            public Person convert(MappingContext<InsertPerson, Person> mappingContext) {
                InsertPerson source = mappingContext.getSource();
                Person destination = new Person();
                Role role = roleRepository.findById(source.getRoleId()).orElseThrow(() ->new NoSuchElementException("Rol bulunamadı"));

                String base64;
                try {
                     base64 = Base64.getEncoder().encodeToString(source.getImage().getBytes());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                destination.setCreatedAt(new Date());
                destination.setEmail(source.getEmail());
                destination.setRole(role);
                destination.setUsername(source.getUsername());
                destination.setPassword(passwordEncoder.encode(source.getPassword()));
                destination.setPhoneNumber(source.getPhoneNumber());
                destination.setImage(base64);
                return destination;
            }
        };

        modelMapper.addConverter(insertPersonPersonConverter);

        Converter<UpdatePerson,Person> updatePersonPersonConverter = new Converter<UpdatePerson, Person>() {
            @Override
            public Person convert(MappingContext<UpdatePerson, Person> mappingContext) {
                UpdatePerson source = mappingContext.getSource();
                Person destination = personRepository.findById(source.getId())
                        .orElseThrow(() -> new NoSuchElementException(source.getId()+" id'li Kullanıcı Bulunamadı"));

                Role role = roleRepository.findById(source.getRoleId()).orElseThrow(() ->new NoSuchElementException("Rol bulunamadı"));
                String base64;
                try {
                    base64 = Base64.getEncoder().encodeToString(source.getImage().getBytes());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                destination.setUsername(source.getUsername());
                destination.setEmail(source.getEmail());
                destination.setPhoneNumber(source.getPhoneNumber());
                destination.setRole(role);
                destination.setImage(base64);
                return destination;
            }
        };

            modelMapper.addConverter(updatePersonPersonConverter);


            Converter<Person, PersonViewModel> personPersonViewModelConverter = new Converter<Person, PersonViewModel>() {
                @Override
                public PersonViewModel convert(MappingContext<Person, PersonViewModel> mappingContext) {
                    Person source = mappingContext.getSource();

                    PersonViewModel destination = new PersonViewModel();

                    destination.setId(source.getId());
                    destination.setUsername(source.getUsername());
                    destination.setEmail(source.getEmail());
                    destination.setPhoneNumber(source.getPhoneNumber());
                    destination.setImage(source.getImage());
                    destination.setCreatedAt(source.getCreatedAt());
                    return destination;
                }
            };

            modelMapper.addConverter(personPersonViewModelConverter);


        return modelMapper;
    }

}
