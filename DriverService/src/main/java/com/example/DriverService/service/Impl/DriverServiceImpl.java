package com.example.DriverService.service.Impl;

import com.example.DriverService.dto.driver.request.InsertDriver;
import com.example.DriverService.dto.driver.request.UpdateDriver;
import com.example.DriverService.dto.driver.response.DriverViewModel;
import com.example.DriverService.entity.Driver;
import com.example.DriverService.entity.DriverPenalty;
import com.example.DriverService.entity.DriverStatus;
import com.example.DriverService.repository.IDriverPenaltyRepository;
import com.example.DriverService.repository.IDriverRepository;
import com.example.DriverService.service.IDriverService;
import com.example.DriverService.util.JwtUtil;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DriverServiceImpl implements IDriverService {

    private static final Logger _logger = LoggerFactory.getLogger(DriverServiceImpl.class);
    private final IDriverRepository driverRepository;
    private final JwtUtil jwtUtil;
    private final ModelMapper modelMapper;
    private final KafkaTemplate<String, Driver> kafkaTemplate;

    public DriverServiceImpl(IDriverRepository driverRepository, JwtUtil jwtUtil, ModelMapper modelMapper, KafkaTemplate<String, Driver> kafkaTemplate) {
        this.driverRepository = driverRepository;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public List<DriverViewModel> getAll() {
        _logger.info("Fetching all drivers.");
        return driverRepository.findAll()
                .stream()
                .map(driver -> modelMapper.map(driver, DriverViewModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public void createDriver(String authHeader, InsertDriver tempDriver) throws Exception {
        String token = authHeader.substring(7);
        Integer userId = jwtUtil.extractUserId(token);
        try {

            Driver driver = modelMapper.map(tempDriver, Driver.class);
            driver.setPersonId(userId);
            driverRepository.save(driver);
            _logger.info("New driver created. UserId: {}", userId);
            kafkaTemplate.send("driver-created", driver);
        } catch (Exception e) {
            _logger.error("Failed to create driver for userId: {}", userId, e);
            throw new Exception(e);
        }
    }
    @Override
    public DriverViewModel findById(UUID id) {
            Driver driver =  driverRepository.findById(id).orElseThrow(() -> new NoSuchElementException(id+" ID'li sürücü bulunamadı"));
            return modelMapper.map(driver,DriverViewModel.class);
    }

    @Override
    public void updateDriver(UUID id, UpdateDriver firstDriver) throws Exception {
        try {
            Driver lastDriver = driverRepository.findByIdAndDeletedFalse(id)
                    .orElseThrow(() -> new NoSuchElementException(id + " ID'li sürücü bulunamadı"));

            modelMapper.map(firstDriver, lastDriver);

            driverRepository.save(lastDriver);

            _logger.info("Driver with ID {} successfully updated.", id);
        } catch (Exception e) {
            _logger.error("An error occurred while updating the driver.", e);
            throw new Exception(e);
        }
    }

    @Override
    public void delete(UUID id) throws Exception {
        try {
            Driver driver = driverRepository.findByIdAndDeletedFalse(id)
                    .orElseThrow(() -> new NoSuchElementException(id + " ID'li sürücü bulunamadı"));
            driver.setDeleted(true);
            driverRepository.save(driver);
            _logger.info("Driver with ID {} has been soft-deleted.", id);
        } catch (Exception e) {
            _logger.error("An error occurred while deleting the driver.", e);
            throw new Exception(e);
        }
    }

    @Override
    public DriverViewModel getCurrentDriver(String autHeader) throws Exception {
        try {
            String token = autHeader.substring(7);
            Integer personId = jwtUtil.extractUserId(token);
            _logger.info("Getting current driver for personId: {}", personId);
            Driver driver = driverRepository.findByPersonIdAndDeletedFalse(personId)
                    .orElseThrow(() -> new NoSuchElementException(personId + " ID'li personId nin Driver'ı bulunamadı"));
            _logger.info("Successfully retrieved driver for personId: {}", personId);
            return modelMapper.map(driver, DriverViewModel.class);
        } catch (Exception e) {
            _logger.error("An error occurred while retrieving current driver.", e);
            throw new Exception(e);
        }
    }

    @Override
    public void updateSalaryByDrivingScore(UUID driverId) throws Exception {
        try {
            Driver driver = driverRepository.findByIdAndDeletedFalse(driverId)
                    .orElseThrow(() -> new NoSuchElementException(driverId + " ID'li driver bulunamadı"));

            Double score = driver.getDrivingScore();
            Double salary = driver.getSalary();

            if (score >= 80 && score <= 100) {
                salary *= 1.5;
            } else if (score >= 60 && score < 80) {
                salary *= 1.3;
            } else if (score == 50) {
                salary *= 1.1;
            } else if (score >= 40 && score < 50) {
                salary *= 0.8;
            }

            driver.setSalary(salary);
            driverRepository.save(driver);

            _logger.info("Updated salary for driver {} based on driving score {}: {}", driverId, score, salary);
        } catch (Exception e) {
            _logger.error("Error updating salary for driver with ID {}", driverId, e);
            throw new Exception(e);
        }

    }

    @Override
    public void updateDriverScore(UUID driverId) throws Exception {
        try {
            Driver driver = driverRepository.findByIdAndDeletedFalse(driverId)
                    .orElseThrow(() -> new NoSuchElementException(driverId + " ID'li driver bulunamadı"));

            List<DriverPenalty> driverPenalties = driver.getDriverPenaltyList();
            Double driverScore = driver.getDrivingScore();
            for (DriverPenalty penalty : driverPenalties) {
                Integer penaltyPoints = penalty.getPenaltyType().getPenaltyPoints();
                driverScore -= penaltyPoints;
            }
            driverScore = Math.max(driverScore, 0);
            driver.setDrivingScore(driverScore);
            _logger.info("Updated driver score for driverId {}: new score = {}", driverId, driverScore);
            driverRepository.save(driver);
        } catch (Exception e) {
            _logger.error("Error while updating driver score for driverId {}: ", driverId, e);
            throw new Exception(e);
        }
    }

    @Override
    public void sendLicenseExpiryWarning(UUID driverId) throws Exception {

        try {
            Driver driver = driverRepository.findByIdAndDeletedFalse(driverId)
                    .orElseThrow(() -> new NoSuchElementException(driverId + " ID'li araç bulunamadı"));
            LocalDate localDate = LocalDate.of(2035, 12, 30);
            Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            if (driver.getLicenseExpiryDate().before(date)) {
                _logger.info("License expired message  ");
                driver.setStatus(DriverStatus.LICENSE_EXPIRED);
                driverRepository.save(driver);
                kafkaTemplate.send("license-expiry-warning", driver);
            }
        } catch (Exception e) {
            _logger.error("An error occured while sending expiry warning for driverId {}:", driverId, e);
            throw new Exception(e);
        }
    }

    @Override
    public void suspendDriver(UUID driverId) throws Exception {
        try {
            Driver driver = driverRepository.findByIdAndDeletedFalse(driverId)
                    .orElseThrow(() -> new NoSuchElementException(driverId + " ID'li driver bulunamadı"));

            if (driver.getDrivingScore() < 40) {
                _logger.info("Driver suspended for traffic violation {} ", driver);
                driver.setStatus(DriverStatus.SUSPENDED);
                driverRepository.save(driver);
                _logger.info("Driver status updated to SUSPENDED for driverId {}", driverId);
                kafkaTemplate.send("suspended-driver", driver);
            }
        } catch (Exception e) {
            _logger.error("Error occurred while suspending driver with ID {}: ", driverId, e);
            throw new Exception(e);
        }

    }
}
