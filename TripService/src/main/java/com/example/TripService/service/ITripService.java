package com.example.TripService.service;

import com.example.TripService.dto.trip.request.InsertTrip;
import com.example.TripService.dto.trip.request.UpdateTrip;
import com.example.TripService.dto.trip.response.TripViewModel;
import com.example.TripService.entity.TripStatus;

import java.util.List;
import java.util.UUID;

public interface ITripService {

    List<TripViewModel> getAllTripsByDriverId(UUID driverId) throws Exception;

    void createTrip(InsertTrip trip) throws Exception;

    void startTrip(UUID tripId) throws Exception;

    void pauseTrip(UUID tripId) throws Exception;

    void resumeTrip(UUID tripId) throws Exception;

    void cancelTrip(UUID tripId) throws Exception;

    void completeTrip(UUID tripId) throws Exception;

    void updateTrip(UUID id,UpdateTrip trip) throws Exception;

    void deleteTrip(UUID tripId) throws Exception;

    List<TripViewModel> getTripsByStatus(TripStatus status) throws Exception;

    List<TripViewModel> getUpcomingTripsByDriverId(UUID driverId) throws Exception;

    Boolean driverAndVehicleIsAssign(UUID vehicleId);
}
