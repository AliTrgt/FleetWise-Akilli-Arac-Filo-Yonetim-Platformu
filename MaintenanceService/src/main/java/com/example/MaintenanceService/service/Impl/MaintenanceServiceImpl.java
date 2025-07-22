package com.example.MaintenanceService.service.Impl;

import com.example.MaintenanceService.client.IVehicleClient;
import com.example.MaintenanceService.dto.client.VehicleViewModel;
import com.example.MaintenanceService.dto.maintenance.request.InsertMaintenance;
import com.example.MaintenanceService.dto.maintenance.request.UpdateMaintenance;
import com.example.MaintenanceService.dto.maintenance.response.MaintenanceViewModel;
import com.example.MaintenanceService.entity.Maintenance;
import com.example.MaintenanceService.entity.MaintenanceStatus;
import com.example.MaintenanceService.repository.IMaintenanceRepository;
import com.example.MaintenanceService.service.IMaintenanceService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MaintenanceServiceImpl implements IMaintenanceService {
    private final Logger _logger = LoggerFactory.getLogger(MaintenanceServiceImpl.class);
    private final IVehicleClient client;
    private final IMaintenanceRepository maintenanceRepository;
    private final ModelMapper modelMapper;
    private final KafkaTemplate<String,Maintenance> kafkaTemplate;

    public MaintenanceServiceImpl(IMaintenanceRepository maintenanceRepository, ModelMapper modelMapper, IVehicleClient client, KafkaTemplate<String, Maintenance> kafkaTemplate) {
        this.maintenanceRepository = maintenanceRepository;
        this.modelMapper = modelMapper;
        this.client = client;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public List<MaintenanceViewModel> getMaintenanceByVehicleId(UUID vehicleId) {
        return maintenanceRepository.findAllByVehicleIdAndDeletedFalse(vehicleId)
                .stream()
                .map(vehicle -> modelMapper.map(vehicle, MaintenanceViewModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<MaintenanceViewModel> getMaintenanceByStatus(MaintenanceStatus status) {
        return maintenanceRepository.findAllByStatusAndDeletedFalse(status)
                .stream()
                .map(maintenance -> modelMapper.map(maintenance, MaintenanceViewModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public void createMaintenance(InsertMaintenance insertMaintenance) throws Exception {
        try {
            _logger.info("Creating maintenance: {}", insertMaintenance);
            Maintenance maintenance = modelMapper.map(insertMaintenance, Maintenance.class);
            maintenanceRepository.save(maintenance);
        } catch (Exception e) {
            _logger.error("Error creating maintenance: {}", e.getMessage(), e);
            throw new Exception(e);
        }
    }

    @Override
    public void updateMaintenance(UUID id, UpdateMaintenance updateMaintenance) throws Exception {
        try {
            _logger.info("Updating maintenance with ID: {}", id);
            Maintenance maintenance = maintenanceRepository.findById(id).orElseThrow(() -> new NoSuchElementException(id + " ID'li bakım bulunamadı"));
            modelMapper.map(updateMaintenance, maintenance);
            maintenanceRepository.save(maintenance);
        } catch (Exception e) {
            _logger.error("Error updating maintenance with ID {}: {}", id, e.getMessage(), e);
            throw new Exception(e);
        }
    }

    @Override
    public void deleteMaintenance(UUID id) throws Exception {
        try {
            _logger.info("Deleting maintenance with ID: {}", id);
            Maintenance maintenance = maintenanceRepository.findById(id).orElseThrow(() -> new NoSuchElementException(id + " ID'li bakım bulunamadı"));
            maintenance.setDeleted(true);
            maintenanceRepository.save(maintenance);
        } catch (Exception e) {
            _logger.error("Error deleting maintenance with ID {}: {}", id, e.getMessage(), e);
            throw new Exception(e);
        }
    }

    @Override
    public MaintenanceViewModel getById(UUID id) throws Exception {
        try {
            _logger.info("Getting maintenance by ID: {}", id);
            Maintenance maintenance = maintenanceRepository.findById(id).orElseThrow(() -> new NoSuchElementException(id + " ID'li bakım bulunamadı"));
            return modelMapper.map(maintenance, MaintenanceViewModel.class);
        } catch (Exception e) {
            _logger.error("Error getting maintenance by ID {}: {}", id, e.getMessage(), e);
            throw new Exception(e);
        }
    }

    @Override
    public void isVehicleOverDue(UUID maintenanceId) throws Exception {
        try {
            Maintenance maintenance = maintenanceRepository.findById(maintenanceId)
                    .orElseThrow(() -> new NoSuchElementException("Maintenance record not found with ID: " + maintenanceId));

            LocalDate today = LocalDate.now();
            if (maintenance.getEstimatedCompletionDate().isBefore(today) && maintenance.getStatus() == MaintenanceStatus.IN_PROGRESS) {
                maintenance.setStatus(MaintenanceStatus.OVERDUE);
                maintenanceRepository.save(maintenance);
                _logger.info("Maintenance status for ID {} was set to OVERDUE.", maintenanceId);
            }
        } catch (Exception e) {
            _logger.error("Error setting maintenance to overdue for ID {}: {}", maintenanceId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void assignMaintenanceToVehicle(UUID maintenanceId, UUID vehicleId) throws Exception {
        _logger.info("Attempting to assign maintenance {} to vehicle {}", maintenanceId, vehicleId);
        try {
            VehicleViewModel vehicleViewModel = client.findById(vehicleId);
            if (vehicleViewModel == null) {
                throw new NoSuchElementException("Vehicle not found with ID: " + vehicleId);
            }

            Maintenance maintenanceToAssign = maintenanceRepository.findByIdAndDeletedFalse(maintenanceId)
                    .orElseThrow(() -> new NoSuchElementException("Maintenance not found with ID: " + maintenanceId));

            maintenanceToAssign.setVehicleId(vehicleId);
            maintenanceToAssign.setConfirmed(true);
            maintenanceToAssign.setStatus(MaintenanceStatus.IN_PROGRESS);
            maintenanceRepository.save(maintenanceToAssign);
            _logger.info("Successfully assigned maintenance {} to vehicle {}.", maintenanceId, vehicleId);

        } catch (Exception e) {
            _logger.error("Failed to assign maintenance {} to vehicle {}: {}", maintenanceId, vehicleId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void unAssignMaintenanceToVehicle(UUID maintenanceId) throws Exception {
        _logger.info("Attempting to unassign maintenance {}", maintenanceId);
        try {
            Maintenance maintenance = maintenanceRepository.findByIdAndDeletedFalse(maintenanceId)
                    .orElseThrow(() -> new NoSuchElementException("Maintenance not found with ID: " + maintenanceId));

            maintenance.setVehicleId(null);
            maintenance.setConfirmed(false);
            maintenance.setStatus(MaintenanceStatus.CANCEL);
            maintenanceRepository.save(maintenance);
            _logger.info("Successfully unassigned maintenance {} and set status to CANCELLED.", maintenanceId);

        } catch (Exception e) {
            _logger.error("Failed to unassign maintenance {}: {}", maintenanceId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void markAsCompleted(UUID id) {
        try {
            Maintenance maintenance = maintenanceRepository.findById(id)
                    .orElseThrow(() ->new NoSuchElementException(id+" ID'li bakım kaydı bulunamadı"));

            LocalDate today = LocalDate.now();
            if ((maintenance.getEstimatedCompletionDate().isEqual(today)) &&
                    maintenance.getStatus() == MaintenanceStatus.IN_PROGRESS) {
                maintenance.setStatus(MaintenanceStatus.COMPLETED);
                maintenanceRepository.save(maintenance);
                _logger.info("Bakım kaydı {} otomatik olarak COMPLETED durumuna geçirildi. Tahmini tamamlanma tarihi: {}",
                        id, maintenance.getEstimatedCompletionDate());
            }
        } catch (Exception e) {
            _logger.error("Bakım kaydı {} tamamlanmış olarak işaretlenirken hata oluştu: {}", id, e.getMessage(), e);
            throw e;
        }
    }
}
