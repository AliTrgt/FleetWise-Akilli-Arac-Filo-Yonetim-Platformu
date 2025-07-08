package com.example.TripService.service;

import com.example.TripService.dto.request.InsertTrip;
import com.example.TripService.dto.response.TripViewModel;
import com.example.TripService.entity.Trip;

import java.util.List;
import java.util.UUID;

public interface ITripService {


    List<TripViewModel> getAllTripByDriverId(UUID driverId) throws Exception;

    void createTrip(InsertTrip trip) throws Exception;

    void removeTrip(UUID tripId);


}
