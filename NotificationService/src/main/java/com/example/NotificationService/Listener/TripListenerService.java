package com.example.NotificationService.Listener;

import com.example.NotificationService.Impl.MailService;
import com.example.NotificationService.client.IDriverClient;
import com.example.NotificationService.client.IUserClient;
import com.example.NotificationService.dto.DriverViewModel;
import com.example.NotificationService.dto.PersonViewModel;
import com.example.NotificationService.dto.TripDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TripListenerService {

    private final ObjectMapper objectMapper;
    private final IDriverClient driverClient;
    private final IUserClient userClient;
    private final MailService mailService;

    public TripListenerService(ObjectMapper objectMapper, IDriverClient driverClient, IUserClient userClient, MailService mailService) {
        this.objectMapper = objectMapper;
        this.driverClient = driverClient;
        this.userClient = userClient;
        this.mailService = mailService;
    }

    @KafkaListener(topics = "trip-created", groupId = "notification-group")
    public void createdTrip(String message) throws Exception {
        try {
            TripDto tripDto = objectMapper.readValue(message, TripDto.class);
            DriverViewModel driverViewModel = driverClient.findById(tripDto.getDriverId());
            PersonViewModel personViewModel = userClient.findById(driverViewModel.getPersonId());
            String subject = "📍 Yeni Bir Yolculuk Oluşturuldu!";
            String body = String.format("""
                    Merhaba %s,
                    
                    Sistemimizde adınıza yeni bir yolculuk oluşturulmuştur.
                    
                    Yolculuk Bilgileri:
                    • Başlangıç Noktası: %s
                    • Bitiş Noktası: %s
                    • Başlangıç Zamanı: %s
                    • Yolculuk Tipi: %s
                    
                    Lütfen sisteme giriş yaparak detayları kontrol edin.
                    
                    Güvenli sürüşler dileriz.  
                    FleetWise Ekibi
                    """, personViewModel.getUsername(), tripDto.getStartLocation(), tripDto.getEndLocation(), tripDto.getStartTime(), tripDto.getTripType());

            mailService.sendMail(personViewModel.getEmail(), subject, body);

        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @KafkaListener(topics = "trip-start", groupId = "notification-group")
    public void startTrip(String message) throws Exception {
        try {
            TripDto tripDto = objectMapper.readValue(message, TripDto.class);
            DriverViewModel driverViewModel = driverClient.findById(tripDto.getDriverId());
            PersonViewModel personViewModel = userClient.findById(driverViewModel.getPersonId());
            String subject = "🚀 Yolculuğunuz Başladı!";
            String body = String.format("""
                    Merhaba %s,
                    
                    Planlanan yolculuğunuz sistem üzerinde başlatılmıştır.
                    
                    • Başlangıç Noktası: %s  
                    • Başlangıç Zamanı: %s  
                    • Araç: %s
                    
                    Lütfen dikkatli sürüş yapın ve kurallara uyun.
                    
                    İyi yolculuklar!  
                    FleetWise Ekibi
                    """, personViewModel.getUsername(), tripDto.getStartLocation(), tripDto.getStartTime(), tripDto.getVehicleId());

            mailService.sendMail(personViewModel.getEmail(), subject, body);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }


    @KafkaListener(topics = "trip-paused", groupId = "notification-group")
    public void pauseTrip(String message) throws Exception {
        try {
            TripDto tripDto = objectMapper.readValue(message, TripDto.class);
            DriverViewModel driverViewModel = driverClient.findById(tripDto.getDriverId());
            PersonViewModel personViewModel = userClient.findById(driverViewModel.getPersonId());
            String subject = "⏸ Yolculuğunuz Duraklatıldı";
            String body = String.format("""
                    Merhaba %s,
                    
                    Devam etmekte olan yolculuğunuz şu anda duraklatılmış olarak işaretlendi.
                    
                    • Başlangıç Noktası: %s  
                    • Duraklatılma Zamanı: %s
                    
                    Gerekli durumlarda yöneticinizle iletişime geçebilirsiniz.
                    
                    İyi günler dileriz.  
                    FleetWise Ekibi
                    """, personViewModel.getUsername(), tripDto.getStartLocation(), tripDto.getEndTime());
            mailService.sendMail(personViewModel.getEmail(), subject, body);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @KafkaListener(topics = "trip-resume", groupId = "notification-group")
    public void resumeTrip(String message) throws Exception {
        try {
            TripDto tripDto = objectMapper.readValue(message, TripDto.class);
            DriverViewModel driverViewModel = driverClient.findById(tripDto.getDriverId());
            PersonViewModel personViewModel = userClient.findById(driverViewModel.getPersonId());
            String subject = "▶️ Yolculuğunuz Devam Ediyor";
            String body = String.format("""
                    Merhaba %s,
                    
                    Duraklatılan yolculuğunuz tekrar başlatıldı ve aktif olarak devam etmektedir.
                    
                    • Güzergâh: %s ➡ %s  
                    • Devam Zamanı: %s
                    
                    Güvenli sürüşler dileriz.  
                    FleetWise Ekibi
                    """, personViewModel.getUsername(), tripDto.getStartLocation(), tripDto.getEndLocation(), tripDto.getStartTime());
            mailService.sendMail(personViewModel.getEmail(), subject, body);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @KafkaListener(topics = "trip-cancel", groupId = "notification-group")
    public void cancelTrip(String message) throws Exception {
        try {
            TripDto tripDto = objectMapper.readValue(message, TripDto.class);
            DriverViewModel driverViewModel = driverClient.findById(tripDto.getDriverId());
            PersonViewModel personViewModel = userClient.findById(driverViewModel.getPersonId());
            String subject = "❌ Yolculuk İptal Edildi";
            String body = String.format("""
                    Merhaba %s,
                    
                    Aşağıdaki yolculuk sistem üzerinden iptal edilmiştir.
                    
                    • Güzergâh: %s ➡ %s  
                    • Planlanan Başlangıç Zamanı: %s  
                    • Yolculuk Tipi: %s
                    
                    İptal gerekçesi hakkında detaylı bilgiye sistemden ulaşabilirsiniz.
                    
                    Anlayışınız için teşekkür ederiz.  
                    FleetWise Ekibi
                    """, personViewModel.getUsername(), tripDto.getStartLocation(), tripDto.getEndLocation(), tripDto.getStartTime(), tripDto.getTripType());
            mailService.sendMail(personViewModel.getEmail(), subject, body);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @KafkaListener(topics = "trip-complete", groupId = "notification-group")
    public void completeTrip(String message) throws Exception {
        try {
            TripDto tripDto = objectMapper.readValue(message, TripDto.class);
            DriverViewModel driverViewModel = driverClient.findById(tripDto.getDriverId());
            PersonViewModel personViewModel = userClient.findById(driverViewModel.getPersonId());
            String subject = "✅ Yolculuk Tamamlandı";
            String body = String.format("""
                    Merhaba %s,
                    
                    Başarıyla tamamlanan yolculuğunuz sistemimize kaydedilmiştir.
                    
                    • Güzergâh: %s ➡ %s  
                    • Başlangıç: %s  
                    • Bitiş: %s  
                    • Yolculuk Tipi: %s
                    
                    Yolculuğunuzla ilgili geri bildirimde bulunmak isterseniz, sistem üzerinden değerlendirme yapabilirsiniz.
                    
                    Başarılı sürüşlerinizin devamını dileriz.  
                    FleetWise Ekibi
                    """, personViewModel.getUsername(), tripDto.getStartLocation(), tripDto.getEndLocation(), tripDto.getStartTime(), tripDto.getEndTime(), tripDto.getTripType());
            mailService.sendMail(personViewModel.getEmail(),subject,body);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }


}
