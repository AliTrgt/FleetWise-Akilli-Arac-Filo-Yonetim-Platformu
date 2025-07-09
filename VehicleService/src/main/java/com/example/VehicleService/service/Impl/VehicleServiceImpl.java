package com.example.VehicleService.service.Impl;

import com.example.VehicleService.client.IDriverClient;
import com.example.VehicleService.dto.driverclient.DriverViewModel;
import com.example.VehicleService.dto.vehicle.request.InsertVehicle;
import com.example.VehicleService.dto.vehicle.request.UpdateVehicle;
import com.example.VehicleService.dto.vehicle.response.VehicleViewModel;
import com.example.VehicleService.entity.Vehicle;
import com.example.VehicleService.entity.VehicleStatus;
import com.example.VehicleService.repository.IVehicleRepository;
import com.example.VehicleService.service.IVehicleService;
import org.apache.kafka.common.protocol.types.Field;
import org.apache.kafka.common.quota.ClientQuotaAlteration;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class VehicleServiceImpl implements IVehicleService {
    private static final Logger _logger = LoggerFactory.getLogger(VehicleServiceImpl.class);
    private final IDriverClient driverClient;
    private final IVehicleRepository vehicleRepository;
    private final ModelMapper modelMapper;
    private final KafkaTemplate<String, Vehicle> kafkaTemplate;

    public VehicleServiceImpl(IDriverClient driverClient, IVehicleRepository vehicleRepository, ModelMapper modelMapper, KafkaTemplate<String, Vehicle> kafkaTemplate) {
        this.driverClient = driverClient;
        this.vehicleRepository = vehicleRepository;
        this.modelMapper = modelMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public List<VehicleViewModel> getAll() {
        return vehicleRepository.findAll()
                .stream()
                .map(vehicle -> modelMapper.map(vehicle, VehicleViewModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public void assignDriverToVehicle(UUID driverId, UUID vehicleId) throws Exception {
        try {

            DriverViewModel driverViewModel = driverClient.findById(driverId);
            if (driverViewModel == null) {
                throw new NoSuchElementException("Driver with id " + driverId + " not found.");
            }

            Optional<Vehicle> optionalVehicle = vehicleRepository.findByDriverIdAndDeletedFalse(driverId);

            if (optionalVehicle.isPresent()) {
                throw new IllegalStateException("Driver with id " + driverId + " is already assigned to vehicle " + optionalVehicle.get().getId());
            }

            Vehicle vehicle = vehicleRepository.findByIdAndDeletedFalse(vehicleId)
                    .orElseThrow(() -> new NoSuchElementException("Vehicle with id " + vehicleId + " not found."));


            vehicle.setDriverId(driverId);
            vehicle.setStatus(VehicleStatus.ASSIGNED);
            vehicle.setAssigned(true);
            vehicleRepository.save(vehicle);

            _logger.info("Successfully assigned driver {} to vehicle {}", driverId, vehicleId);
            kafkaTemplate.send("assign-driver-vehicle", vehicle);
        } catch (NoSuchElementException e) {
            _logger.error("Assignment failed due to missing entity: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            _logger.error("Unexpected error while assigning driver {} to vehicle {}", driverId, vehicleId, e);
            throw e;
        }
    }

    @Override
    public void unAssignDriverToVehicle(UUID vehicleId) throws Exception {
        try {
            Vehicle vehicle = vehicleRepository.findByIdAndDeletedFalse(vehicleId)
                    .orElseThrow(() -> new NoSuchElementException("Vehicle with id " + vehicleId + " not found."));
            UUID prevDriverId = vehicle.getDriverId();
            vehicle.setDriverId(null);
            vehicle.setStatus(VehicleStatus.ACTIVE);
            vehicleRepository.save(vehicle);
            _logger.info("Successfully unassigned vehicle :  {} driverId : {} ", vehicleId, prevDriverId);
            kafkaTemplate.send("unAssign-driver-vehicle", vehicle);
        } catch (NoSuchElementException e) {
            _logger.error("Assignment failed due to missing entity: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            _logger.error("Unexpected error while unassigning vehicle {}", vehicleId, e);
            throw e;
        }
    }

    @Override
    public void insert(String authHeader, InsertVehicle insertVehicle) throws Exception {
        try {
            Vehicle vehicle = modelMapper.map(insertVehicle, Vehicle.class);
            vehicleRepository.save(vehicle);
            _logger.info("Vehicle created : {}", vehicle);

        } catch (Exception e) {
            _logger.error("Failed to create Vehicle for vehicleId ", e);
            throw new Exception(e);
        }
    }

    @Override
    public void update(UUID vehicleId, UpdateVehicle updateVehicle) throws Exception {
        try {
            Vehicle vehicle = vehicleRepository.findByIdAndDeletedFalse(vehicleId)
                    .orElseThrow(() -> new NoSuchElementException(" ID'li araç bulunamadı"));

            modelMapper.map(updateVehicle, vehicle);

            vehicleRepository.save(vehicle);
            _logger.info("Vehicle with ID {} successfully updated.", vehicleId);
        } catch (Exception e) {
            _logger.error("Failed to update vehicle : {} ", vehicleId);
            throw new Exception(e);
        }

    }

    @Override
    public void delete(UUID id) throws Exception {
        try {
            Vehicle vehicle = vehicleRepository.findByIdAndDeletedFalse(id)
                    .orElseThrow(() -> new NoSuchElementException(" ID'li araç bulunamadı"));
            vehicle.setDeleted(true);
            vehicleRepository.save(vehicle);
            _logger.info("Vehicle successfully deleted : {}", vehicle);
        } catch (Exception e) {
            _logger.error("Failed to delete Vehicle for vehicleId  : {}", id);
            throw new Exception(e);
        }
    }

    @Override
    public List<VehicleViewModel> findByDriverId(UUID driverId) {
        return vehicleRepository.findVehicleByDriverIdAndDeletedFalse(driverId)
                .stream()
                .map(vehicle -> modelMapper.map(vehicle, VehicleViewModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public VehicleViewModel findByBrand(String brand) {
        Vehicle vehicles = vehicleRepository.findVehicleByBrandAndDeletedFalse(brand)
                .orElseThrow(() -> new NoSuchElementException(brand + " markasına ait araç kaydı bulunamadı"));
        return modelMapper.map(vehicles, VehicleViewModel.class);
    }

    @Override
    public VehicleViewModel findByStatus(VehicleStatus status) {
        Vehicle vehicle = vehicleRepository.findVehicleByStatusAndDeletedFalse(status)
                .orElseThrow(() -> new NoSuchElementException(status + " durumunda araç bulunamadı"));

        return modelMapper.map(vehicle, VehicleViewModel.class);
    }

    @Override
    public VehicleViewModel findByPlateNumber(String plateNumber) {
        Vehicle vehicle = vehicleRepository.findByPlateNumberAndDeletedFalse(plateNumber)
                .orElseThrow(() -> new NoSuchElementException(plateNumber + " plakalı araç bulunamadı"));
        return modelMapper.map(vehicle, VehicleViewModel.class);
    }

    @Override
    public VehicleViewModel findVehicleByIdAndAssignedTrue(UUID vehicleId) {
        Vehicle vehicle = vehicleRepository.findVehicleByIdAndAssignedTrueAndDeletedFalse(vehicleId)
                .orElseThrow(() -> new NoSuchElementException(vehicleId + " ID'li araç bulunamadı"));
        return modelMapper.map(vehicle, VehicleViewModel.class);
    }


}
