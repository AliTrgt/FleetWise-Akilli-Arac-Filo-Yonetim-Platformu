package com.example.TripService.controller;

import com.example.TripService.dto.trip.request.InsertTrip;
import com.example.TripService.dto.trip.request.UpdateTrip;
import com.example.TripService.dto.trip.response.TripViewModel;
import com.example.TripService.entity.TripStatus;
import com.example.TripService.service.ITripService;
import com.thoughtworks.xstream.converters.ErrorWritingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/trip")
public class TripController {
    private final ITripService tripService;

    public TripController(ITripService tripService) {
        this.tripService = tripService;
    }

    @GetMapping("/driver/{id}")
    public ResponseEntity<List<TripViewModel>> getAllTripsByDriverId(@PathVariable UUID id) throws Exception {
        var trip = tripService.getAllTripsByDriverId(id);
        return ResponseEntity.ok(trip);
    }

    @GetMapping("/status")
    public ResponseEntity<List<TripViewModel>> getTripsByStatus(@RequestParam TripStatus tripStatus) throws Exception {
        var trip = tripService.getTripsByStatus(tripStatus);
        return ResponseEntity.ok(trip);
    }

    @GetMapping("/upcoming/{driverId}")
    public ResponseEntity<List<TripViewModel>> getUpcomingTripsByDriverId(@PathVariable UUID driverId) throws Exception {
        var trip = tripService.getUpcomingTripsByDriverId(driverId);
        return ResponseEntity.ok(trip);
    }

    @PostMapping
    public ResponseEntity<?> createTrip(@RequestBody InsertTrip insertTrip) throws Exception {
        tripService.createTrip(insertTrip);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{tripId}/start")
    public ResponseEntity<?> startTrip(@PathVariable UUID tripId) throws Exception {
        tripService.startTrip(tripId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{tripId}/pause")
    public ResponseEntity<?> pauseTrip(@PathVariable UUID tripId) throws Exception {
        tripService.pauseTrip(tripId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{tripId}/resume")
    public ResponseEntity<?> resume(@PathVariable UUID tripId) throws Exception {
        tripService.resumeTrip(tripId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{tripId}/cancel")
    public ResponseEntity<?> cancelTrip(@PathVariable UUID tripId) throws Exception {
        tripService.cancelTrip(tripId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{tripId}/complete")
    public ResponseEntity<?> completeTrip(@PathVariable UUID tripId) throws Exception {
        tripService.completeTrip(tripId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTrip(@PathVariable UUID id, @RequestBody UpdateTrip updateTrip) throws Exception {
        tripService.updateTrip(id, updateTrip);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{tripId}")
    public ResponseEntity<?> deleteTrip(@PathVariable UUID tripId) throws Exception {
        tripService.deleteTrip(tripId);
        return ResponseEntity.ok().build();
    }

}
