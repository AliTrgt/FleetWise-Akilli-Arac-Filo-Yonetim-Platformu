package com.example.NotificationService.Listener;

import com.example.NotificationService.Impl.MailService;
import com.example.NotificationService.client.IDriverClient;
import com.example.NotificationService.client.IUserClient;
import com.example.NotificationService.dto.DriverViewModel;
import com.example.NotificationService.dto.PersonViewModel;
import com.example.NotificationService.dto.VehicleDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class VehicleListenerService {
    private static final Logger _logger = LoggerFactory.getLogger(VehicleListenerService.class);

    private final ObjectMapper objectMapper;
    private final MailService mailService;
    private final IDriverClient driverClient;
    private final IUserClient userClient;

    public VehicleListenerService(ObjectMapper objectMapper, MailService mailService, IDriverClient driverClient, IUserClient userClient) {
        this.objectMapper = objectMapper;
        this.mailService = mailService;
        this.driverClient = driverClient;
        this.userClient = userClient;
    }

    @KafkaListener(topics = "assign-driver-vehicle", groupId = "notification-group")
    public void assignedDriverToVehicle(String message) throws Exception {
        try {
            VehicleDto vehicleDto = objectMapper.readValue(message, VehicleDto.class);
            DriverViewModel driverViewModel = driverClient.findById(vehicleDto.getDriverId());
            PersonViewModel personViewModel = userClient.findById(driverViewModel.getPersonId());

            String subject = "🚗 Yeni Bir Araç Size Atandı!";
            String body = String.format("""
                Merhaba %s,

                Sistemimizde aşağıdaki özelliklere sahip bir araç size atanmıştır:

                • Plaka: %s  
                • Marka: %s  
                • Model: %s  
                • Renk: %s  
                • Araç Tipi: %s  
                • Kilometre: %.0f km

                Aracı kullanmadan önce genel kontrolünü yapmayı ve varsa sorunları bildirmenizi rica ederiz.

                Güvenli sürüşler dileriz.  
                FleetWise Ekibi
                """,
                    personViewModel.getUsername(),
                    vehicleDto.getPlateNumber(),
                    vehicleDto.getBrand(),
                    vehicleDto.getModel(),
                    vehicleDto.getColor(),
                    vehicleDto.getVehicleType(),
                    vehicleDto.getMileage() != null ? vehicleDto.getMileage() : 0
            );

            mailService.sendMail(personViewModel.getEmail(), subject, body);

        } catch (Exception e) {
            _logger.error("Araç atama bildirimi gönderilemedi", e);
            throw new Exception(e);
        }
    }

    @KafkaListener(topics = "unAssign-driver-vehicle", groupId = "notification-group")
    public void unAssignedDriverToVehicle(String message) throws Exception {
        try {
            VehicleDto vehicleDto = objectMapper.readValue(message, VehicleDto.class);
        }catch (Exception e){
            throw new Exception(e);
        }
    }


}
