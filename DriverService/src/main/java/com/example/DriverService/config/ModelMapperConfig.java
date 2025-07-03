package com.example.DriverService.config;

import com.example.DriverService.dto.driver.request.InsertDriver;
import com.example.DriverService.dto.driver.request.UpdateDriver;
import com.example.DriverService.dto.driver.response.DriverViewModel;
import com.example.DriverService.dto.driverpenalty.request.InsertDriverPenalty;
import com.example.DriverService.dto.driverpenalty.request.UpdateDriverPenalty;
import com.example.DriverService.dto.driverpenalty.response.DriverPenaltyViewModel;
import com.example.DriverService.entity.Driver;
import com.example.DriverService.entity.DriverPenalty;
import com.example.DriverService.entity.DriverStatus;
import com.example.DriverService.entity.PenaltyType;
import com.example.DriverService.repository.IDriverRepository;
import jdk.jshell.Snippet;
import org.apache.commons.configuration.event.ConfigurationErrorEvent;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MappingContext;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.NoSuchElementException;
import java.util.UUID;

@Configuration
public class ModelMapperConfig {

    private final IDriverRepository driverRepository;

    public ModelMapperConfig(IDriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }


    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        Converter<InsertDriver, Driver> insertDriverDriverConverter = new Converter<InsertDriver, Driver>() {
            @Override
            public Driver convert(MappingContext<InsertDriver, Driver> mappingContext) {
                InsertDriver source = mappingContext.getSource();
                Driver destination = new Driver();

                destination.setAddress(source.getAddress());
                destination.setLicenseNumber(source.getLicenseNumber());
                destination.setLicenseExpiryDate(source.getLicenseExpiryDate());
                destination.setStatus(DriverStatus.ACTIVE);
                destination.setDrivingScore(100.0);
                destination.setSalary(22400.0);

                return destination;
            }
        };

        modelMapper.addConverter(insertDriverDriverConverter);

        Converter<UpdateDriver, Driver> updateDriverDriverConverter = new Converter<UpdateDriver, Driver>() {
            @Override
            public Driver convert(MappingContext<UpdateDriver, Driver> mappingContext) {
                UpdateDriver source = mappingContext.getSource();
                Driver destination = mappingContext.getDestination();

                if (source.getAddress() != null){
                    destination.setAddress(source.getAddress());
                }

                if (source.getLicenseNumber() != null){
                    destination.setLicenseNumber(source.getLicenseNumber());
                }

                if (source.getLicenseExpiryDate() != null) {
                    destination.setLicenseExpiryDate(source.getLicenseExpiryDate());
                }
                if (source.getStatus() != null) {
                    destination.setLicenseExpiryDate(source.getLicenseExpiryDate());
                }

                return destination;
            }
        };
        modelMapper.addConverter(updateDriverDriverConverter);

        Converter<Driver, DriverViewModel> driverDriverViewModelConverter = new Converter<Driver, DriverViewModel>() {
            @Override
            public DriverViewModel convert(MappingContext<Driver, DriverViewModel> mappingContext) {
                Driver source = mappingContext.getSource();
                DriverViewModel destination = new DriverViewModel();

                destination.setId(source.getId());
                destination.setAddress(source.getAddress());
                destination.setSalary(source.getSalary());
                destination.setLicenseNumber(source.getLicenseNumber());
                destination.setPersonId(source.getPersonId());
                destination.setDrivingScore(source.getDrivingScore());
                destination.setLicenseExpiryDate(source.getLicenseExpiryDate());
                destination.setDeleted(source.getDeleted());
                destination.setStatus(source.getStatus().toString());

                return destination;
            }
        };
        modelMapper.addConverter(driverDriverViewModelConverter);


        Converter<InsertDriverPenalty, DriverPenalty> insertDriverPenaltyDriverPenaltyConverter = new Converter<InsertDriverPenalty, DriverPenalty>() {
            @Override
            public DriverPenalty convert(MappingContext<InsertDriverPenalty, DriverPenalty> mappingContext) {
                InsertDriverPenalty source = mappingContext.getSource();
                DriverPenalty destination = new DriverPenalty();

                Driver driver = driverRepository.findByIdAndDeletedFalse(source.getDriverId())
                        .orElseThrow(() ->new NoSuchElementException(source.getDriverId()+" ID'li sürücü bulunamadı"));

                destination.setDriver(driver);
                destination.setDescription(source.getDescription());
                destination.setPenaltyType(PenaltyType.valueOf(source.getPenaltyType()));
                return destination;
            }
        };

        modelMapper.addConverter(insertDriverPenaltyDriverPenaltyConverter);

        Converter<UpdateDriverPenalty,DriverPenalty> updateDriverPenaltyDriverPenaltyConverter = new Converter<UpdateDriverPenalty, DriverPenalty>() {
            @Override
            public DriverPenalty convert(MappingContext<UpdateDriverPenalty, DriverPenalty> mappingContext) {
                UpdateDriverPenalty source = mappingContext.getSource();
                DriverPenalty destination = mappingContext.getDestination();


                destination.setPenaltyType(PenaltyType.valueOf(source.getPenaltyType()));
                destination.setDescription(source.getDescription());

                return destination;
            }
        };
        modelMapper.addConverter(updateDriverPenaltyDriverPenaltyConverter);

        Converter<DriverPenalty, DriverPenaltyViewModel> driverPenaltyDriverPenaltyViewModelConverter = new Converter<DriverPenalty, DriverPenaltyViewModel>() {
            @Override
            public DriverPenaltyViewModel convert(MappingContext<DriverPenalty, DriverPenaltyViewModel> mappingContext) {
                DriverPenalty source = mappingContext.getSource();
                DriverPenaltyViewModel destination = new DriverPenaltyViewModel();
                UUID driverId = source.getDriver().getId();

                destination.setId(source.getId());
                destination.setDriverId(driverId);
                destination.setPenaltyType(source.getPenaltyType());
                destination.setDescription(source.getDescription());
                destination.setEventTime(source.getEventTime());

                return destination;
            }
        };

        modelMapper.addConverter(driverPenaltyDriverPenaltyViewModelConverter);

        return modelMapper;
    }

}
