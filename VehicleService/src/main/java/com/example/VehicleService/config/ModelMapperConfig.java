package com.example.VehicleService.config;

import com.example.VehicleService.dto.vehicle.request.InsertVehicle;
import com.example.VehicleService.dto.vehicle.request.UpdateVehicle;
import com.example.VehicleService.dto.vehicle.response.VehicleViewModel;
import com.example.VehicleService.entity.Vehicle;
import com.example.VehicleService.entity.VehicleStatus;
import com.example.VehicleService.entity.VehicleType;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.internal.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import org.modelmapper.internal.bytebuddy.dynamic.loading.InjectionClassLoader;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);


        Converter<InsertVehicle, Vehicle> insertVehicleVehicleConverter = new Converter<InsertVehicle, Vehicle>() {
            @Override
            public Vehicle convert(MappingContext<InsertVehicle, Vehicle> mappingContext) {
                InsertVehicle source = mappingContext.getSource();
                Vehicle destination = new Vehicle();

                destination.setModel(source.getModel());
                destination.setColor(source.getColor());
                destination.setMileage(source.getMileage());
                destination.setPlateNumber(source.getPlateNumber());
                destination.setBrand(source.getBrand());
                destination.setStatus(VehicleStatus.ACTIVE);
                destination.setVehicleType(source.getVehicleType());

                return destination;
            }
        };

        modelMapper.addConverter(insertVehicleVehicleConverter);


        Converter<UpdateVehicle,Vehicle> updateVehicleVehicleConverter = new Converter<UpdateVehicle, Vehicle>() {
            @Override
            public Vehicle convert(MappingContext<UpdateVehicle, Vehicle> mappingContext) {
                UpdateVehicle source = mappingContext.getSource();
                Vehicle destination = mappingContext.getDestination();

                destination.setVehicleType(source.getVehicleType());
                destination.setStatus(source.getStatus());


                return destination;
            }
        };

        modelMapper.addConverter(updateVehicleVehicleConverter);

        Converter<Vehicle, VehicleViewModel> vehicleVehicleViewModelConverter = new Converter<Vehicle, VehicleViewModel>() {
            @Override
            public VehicleViewModel convert(MappingContext<Vehicle, VehicleViewModel> mappingContext) {
                Vehicle source = mappingContext.getSource();
                VehicleViewModel destination = new VehicleViewModel();

                destination.setId(source.getId());
                destination.setModel(source.getModel());
                destination.setVehicleType(source.getVehicleType());
                destination.setBrand(source.getBrand());
                destination.setMileage(source.getMileage());
                destination.setColor(source.getColor());
                destination.setStatus(source.getStatus());
                destination.setDriverId(source.getDriverId());
                destination.setPlateNumber(source.getPlateNumber());
                destination.setDeleted(source.getDeleted());

                return destination;
            }
        };

        modelMapper.addConverter(vehicleVehicleViewModelConverter);


        return modelMapper;
    }



}
