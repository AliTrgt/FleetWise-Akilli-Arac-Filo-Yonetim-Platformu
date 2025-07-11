package com.example.TripService.service.Impl;

import com.example.TripService.client.IVehicleClient;
import com.example.TripService.dto.trip.request.InsertTrip;
import com.example.TripService.dto.trip.request.UpdateTrip;
import com.example.TripService.dto.trip.response.TripViewModel;
import com.example.TripService.entity.Trip;
import com.example.TripService.entity.TripStatus;
import com.example.TripService.repository.ITripRepository;
import com.example.TripService.service.ITripService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TripServiceImpl implements ITripService {
    private static final Logger _logger = LoggerFactory.getLogger(TripServiceImpl.class);
    private final IVehicleClient vehicleClient;
    private final ITripRepository tripRepository;
    private final ModelMapper modelMapper;
    private final KafkaTemplate<String,Trip> kafkaTemplate;

    public TripServiceImpl(IVehicleClient vehicleClient, ITripRepository tripRepository, ModelMapper modelMapper, KafkaTemplate<String, Trip> kafkaTemplate) {
        this.vehicleClient = vehicleClient;
        this.tripRepository = tripRepository;
        this.modelMapper = modelMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public List<TripViewModel> getAllTripsByDriverId(UUID driverId) throws Exception {
        _logger.info("Getting all trips for driver ID: {}", driverId);
        return tripRepository.getAllTripsByDriverIdAndDeletedFalse(driverId)
                .stream()
                .map(trip -> modelMapper.map(trip, TripViewModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public void createTrip(InsertTrip insertTrip) throws Exception {
        try {
            Trip newTrip = modelMapper.map(insertTrip, Trip.class);
            if (driverAndVehicleIsAssign(newTrip.getVehicleId()).equals(true)) {
                if (tripRepository.existsByDriverIdAndVehicleIdAndDeletedFalse(newTrip.getDriverId(),newTrip.getVehicleId()).equals(false)){
                    newTrip.setStatus(TripStatus.PENDING);
                    tripRepository.save(newTrip);
                    _logger.info("Trip created successfully with ID: {}", newTrip.getId());
                    kafkaTemplate.send("trip-created",newTrip);
                }else throw new NoSuchElementException("Bu araç daha önce rotaya başlamış");

            } else throw new NoSuchElementException("Araçlar eşleşmemiş durumda");
        } catch (Exception e) {
            _logger.error("Error creating trip with vehicle ID: {}", insertTrip.getVehicleId(), e);
            throw new Exception(e);
        }
    }

    @Override
    public void startTrip(UUID tripId) throws Exception {
        try {
            Trip trip = tripRepository.findById(tripId)
                    .orElseThrow(() -> new NoSuchElementException(tripId + " ID'li rota bulunamadı"));
            if (driverAndVehicleIsAssign(trip.getVehicleId()).equals(true)) {
                trip.setStatus(TripStatus.STARTED);
                tripRepository.save(trip);
                _logger.info("Trip started successfully with ID: {}", tripId);
                kafkaTemplate.send("trip-start",trip);
            } else throw new NoSuchElementException("Araçlar eşleşmemiş durumda");
        } catch (Exception e) {
            _logger.error("Error starting trip with ID: {}", tripId, e);
            throw new Exception(e);
        }
    }

    @Override
    public void pauseTrip(UUID tripId) throws Exception {
        try {
            Trip trip = tripRepository.findById(tripId)
                    .orElseThrow(() -> new NoSuchElementException(tripId + " ID'li rota bulunamadı"));
            if (driverAndVehicleIsAssign(trip.getVehicleId()).equals(true)) {
                trip.setStatus(TripStatus.PAUSED);
                tripRepository.save(trip);
                _logger.info("Trip paused successfully with ID: {}", tripId);
                kafkaTemplate.send("trip-paused",trip);
            } else throw new NoSuchElementException("Araçlar eşleşmemiş durumda");
        } catch (Exception e) {
            _logger.error("Error pausing trip with ID: {}", tripId, e);
            throw new Exception(e);
        }
    }

    @Override
    public void resumeTrip(UUID tripId) throws Exception {
        try {
            Trip trip = tripRepository.findById(tripId)
                    .orElseThrow(() -> new NoSuchElementException(tripId + " ID'li rota bulunamadı"));
            if (driverAndVehicleIsAssign(trip.getVehicleId()).equals(true)) {
                trip.setStatus(TripStatus.RESUMED);
                tripRepository.save(trip);
                _logger.info("Trip resumed successfully with ID: {}", tripId);
                kafkaTemplate.send("trip-resume",trip);
            } else throw new NoSuchElementException("Araçlar eşleşmemiş durumda");
        } catch (Exception e) {
            _logger.error("Error resuming trip with ID: {}", tripId, e);
            throw new Exception(e);
        }
    }

    @Override
    public void cancelTrip(UUID tripId) throws Exception {
        try {
            Trip trip = tripRepository.findById(tripId)
                    .orElseThrow(() -> new NoSuchElementException(tripId + " ID'li rota bulunamadı"));
            if (driverAndVehicleIsAssign(trip.getVehicleId()).equals(true)) {
                trip.setStatus(TripStatus.CANCELLED);
                tripRepository.save(trip);
                tripRepository.deleteById(tripId);
                _logger.info("Trip canceled successfully with ID: {}", tripId);
                kafkaTemplate.send("trip-cancel",trip);
            } else throw new NoSuchElementException("Araçlar eşleşmemiş durumda");
        } catch (Exception e) {
            _logger.error("Error canceling trip with ID: {}", tripId, e);
            throw new Exception(e);
        }
    }

    @Override
    public void completeTrip(UUID tripId) throws Exception {
        try {
            Trip trip = tripRepository.findById(tripId)
                    .orElseThrow(() -> new NoSuchElementException(tripId + " ID'li rota bulunamadı"));
            if (driverAndVehicleIsAssign(trip.getVehicleId()).equals(true)) {
                trip.setStatus(TripStatus.COMPLETED);
                tripRepository.save(trip);
                _logger.info("Trip completed successfully with ID: {}", tripId);
                kafkaTemplate.send("trip-complete",trip);
            } else throw new NoSuchElementException("Araçlar eşleşmemiş durumda");
        } catch (Exception e) {
            _logger.error("Error completing trip with ID: {}", tripId, e);
            throw new Exception(e);
        }
    }

    @Override
    public void updateTrip(UUID id, UpdateTrip updateTrip) throws Exception {
        try {
            Trip trip = tripRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException(id + " ID'li rota bulunamadı"));
            if (driverAndVehicleIsAssign(trip.getVehicleId()).equals(true)){
                modelMapper.map(updateTrip, trip);
                tripRepository.save(trip);
                _logger.info("Trip updated successfully with ID: {}", id);
            }
            else throw new NoSuchElementException("Araçlar eşleşmemiş durumda güncellleme");
        } catch (Exception e) {
            _logger.error("Error updating trip with ID: {}", id, e);
            throw new Exception(e);
        }
    }

    @Override
    public void deleteTrip(UUID tripId) throws Exception {
        try {
            Trip trip = tripRepository.findById(tripId)
                    .orElseThrow(() -> new NoSuchElementException(tripId + " ID'li rota bulunamadı"));
            trip.setDeleted(true);
            tripRepository.save(trip);
            _logger.info("Trip deleted successfully with ID: {}", tripId);
        } catch (Exception e) {
            _logger.error("Error deleting trip with ID: {}", tripId, e);
            throw new Exception(e);
        }
    }

    @Override
    public List<TripViewModel> getTripsByStatus(TripStatus status) throws Exception {
        _logger.info("Getting trips by status: {}", status);
        return tripRepository.getAllTripsByStatusAndDeletedFalse(status)
                .stream()
                .map(trip -> modelMapper.map(trip, TripViewModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<TripViewModel> getUpcomingTripsByDriverId(UUID driverId) throws Exception {
        _logger.info("Getting upcoming trips for driver ID: {}", driverId);
        return tripRepository.getAllTripsByDriverIdAndStatusAndDeletedFalse(driverId, TripStatus.SCHEDULED)
                .stream()
                .map(trip -> modelMapper.map(trip, TripViewModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public Boolean driverAndVehicleIsAssign(UUID vehicleId) {
        return vehicleClient.findVehicleByIdAndAssignedTrue(vehicleId)
                .getAssigned();
    }
}
