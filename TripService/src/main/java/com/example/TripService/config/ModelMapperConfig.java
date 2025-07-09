package com.example.TripService.config;

import com.example.TripService.dto.trip.request.InsertTrip;
import com.example.TripService.dto.trip.request.UpdateTrip;
import com.example.TripService.dto.trip.response.TripViewModel;
import com.example.TripService.entity.Trip;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        Converter<InsertTrip, Trip> insertTripTripConverter = new Converter<InsertTrip, Trip>() {
            @Override
            public Trip convert(MappingContext<InsertTrip, Trip> mappingContext) {
                InsertTrip source = mappingContext.getSource();
                Trip destination = new Trip();

                destination.setDriverId(source.getDriverId());
                destination.setVehicleId(source.getVehicleId());
                destination.setStartTime(source.getStartTime());
                destination.setEndTime(source.getEndTime());
                destination.setStartLocation(source.getStartLocation());
                destination.setEndLocation(source.getEndLocation());
                destination.setTripType(source.getTripType());
                return destination;
            }
        };
        modelMapper.addConverter(insertTripTripConverter);

        Converter<UpdateTrip,Trip> updateTripTripConverter = new Converter<UpdateTrip, Trip>() {
            @Override
            public Trip convert(MappingContext<UpdateTrip, Trip> mappingContext) {
                UpdateTrip source = mappingContext.getSource();
                Trip destination = mappingContext.getDestination();

                destination.setDriverId(source.getDriverId());
                destination.setVehicleId(source.getVehicleId());
                destination.setStartTime(source.getStartTime());
                destination.setEndTime(source.getEndTime());
                destination.setStartLocation(source.getStartLocation());
                destination.setEndLocation(source.getEndLocation());
                destination.setTripType(source.getTripType());

                return destination;
            }
        };

        modelMapper.addConverter(updateTripTripConverter);


        Converter<Trip, TripViewModel> tripTripViewModelConverter = new Converter<Trip, TripViewModel>() {
            @Override
            public TripViewModel convert(MappingContext<Trip, TripViewModel> mappingContext) {
                Trip source = mappingContext.getSource();
                TripViewModel destination = new TripViewModel();

                destination.setId(source.getId());
                destination.setDriverId(source.getDriverId());
                destination.setVehicleId(source.getVehicleId());
                destination.setStartTime(source.getStartTime());
                destination.setEndTime(source.getEndTime());
                destination.setStartLocation(source.getStartLocation());
                destination.setEndLocation(source.getEndLocation());
                destination.setStatus(source.getStatus());
                destination.setTripType(source.getTripType());
                destination.setStatus(source.getStatus());
                destination.setDeleted(source.getDeleted());

                return destination;
            }
        };

        modelMapper.addConverter(tripTripViewModelConverter);



        return modelMapper;
    }

}
