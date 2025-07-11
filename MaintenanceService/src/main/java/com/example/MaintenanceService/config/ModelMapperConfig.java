package com.example.MaintenanceService.config;

import com.example.MaintenanceService.dto.maintenance.request.InsertMaintenance;
import com.example.MaintenanceService.dto.maintenance.request.UpdateMaintenance;
import com.example.MaintenanceService.dto.maintenance.response.MaintenanceViewModel;
import com.example.MaintenanceService.entity.Maintenance;
import com.example.MaintenanceService.entity.MaintenanceStatus;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Base64;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        Converter<InsertMaintenance, Maintenance> insertMaintenanceMaintenanceConverter = new Converter<InsertMaintenance, Maintenance>() {
            @Override
            public Maintenance convert(MappingContext<InsertMaintenance, Maintenance> mappingContext) {
                InsertMaintenance source = mappingContext.getSource();
                Maintenance destination = new Maintenance();

                String base64;
                try {
                    base64 = Base64.getEncoder().encodeToString(source.getImage().getBytes());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                destination.setStatus(MaintenanceStatus.CREATED);
                destination.setMaintenanceType(source.getMaintenanceType());
                destination.setMaintenanceDate(source.getMaintenanceDate());
                destination.setEstimatedCompletionDate(source.getMaintenanceDate().plusDays(source.getMaintenanceType().getEstimatedDays()));
                destination.setCost(source.getCost());
                destination.setDescription(source.getDescription());
                destination.setImage(base64);

                return destination;
            }
        };
        modelMapper.addConverter(insertMaintenanceMaintenanceConverter);


        Converter<UpdateMaintenance, Maintenance> updateMaintenanceMaintenanceConverter = new Converter<UpdateMaintenance, Maintenance>() {
            @Override
            public Maintenance convert(MappingContext<UpdateMaintenance, Maintenance> mappingContext) {
                UpdateMaintenance source = mappingContext.getSource();
                Maintenance destination = mappingContext.getDestination();

                destination.setCost(source.getCost());
                destination.setStatus(source.getStatus());

                return destination;
            }
        };
        modelMapper.addConverter(updateMaintenanceMaintenanceConverter);

        Converter<Maintenance, MaintenanceViewModel> maintenanceMaintenanceViewModelConverter = new Converter<Maintenance, MaintenanceViewModel>() {
            @Override
            public MaintenanceViewModel convert(MappingContext<Maintenance, MaintenanceViewModel> mappingContext) {
                Maintenance source = mappingContext.getSource();
                MaintenanceViewModel destination = new MaintenanceViewModel();

                destination.setId(source.getId());
                destination.setVehicleId(source.getVehicleId());
                destination.setMaintenanceType(source.getMaintenanceType());
                destination.setMaintenanceDate(source.getMaintenanceDate());
                destination.setEstimatedCompletionDate(source.getEstimatedCompletionDate());
                destination.setCost(source.getCost());
                destination.setDescription(source.getDescription());
                destination.setStatus(source.getStatus());
                destination.setImage(source.getImage());
                destination.setDeleted(source.getDeleted());
                destination.setConfirmed(source.getConfirmed());

                return destination;
            }
        };

        modelMapper.addConverter(maintenanceMaintenanceViewModelConverter);

        return modelMapper;
    }

}
