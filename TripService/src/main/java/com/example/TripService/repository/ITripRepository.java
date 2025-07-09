package com.example.TripService.repository;

import com.example.TripService.entity.Trip;
import com.example.TripService.entity.TripStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ITripRepository extends JpaRepository<Trip, UUID> {
    List<Trip> getAllTripsByDriverIdAndDeletedFalse(UUID driverId);
    List<Trip> getAllTripsByStatusAndDeletedFalse(TripStatus status);
    List<Trip> getAllTripsByDriverIdAndStatusAndDeletedFalse(UUID driverId,TripStatus status);
    Boolean existsByDriverIdAndVehicleIdAndDeletedFalse(UUID driverId,UUID vehicleId);

}
