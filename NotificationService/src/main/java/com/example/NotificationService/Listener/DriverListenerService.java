package com.example.NotificationService.Listener;

import com.example.NotificationService.Impl.MailService;
import com.example.NotificationService.client.IUserClient;
import com.example.NotificationService.dto.DriverViewModel;
import com.example.NotificationService.dto.PersonViewModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class DriverListenerService {

    private final ObjectMapper objectMapper;
    private final IUserClient IUserClient;
    private final MailService mailService;

    public DriverListenerService(ObjectMapper objectMapper, IUserClient IUserClient, MailService mailService) {
        this.objectMapper = objectMapper;
        this.IUserClient = IUserClient;
        this.mailService = mailService;
    }

    @KafkaListener(topics = "driver-created", groupId = "notification-group")
    public void listenDriverCreated(String message) throws Exception {
        try {
            DriverViewModel driverViewModel = objectMapper.readValue(message, DriverViewModel.class);
            PersonViewModel p = IUserClient.findById(driverViewModel.getPersonId());
            String subject = "🚗 FleetWise'e Hoş Geldiniz – Sürücü Hesabınız Oluşturuldu!";
            String body = String.format("""
                    Merhaba %s,
                    
                    FleetWise sistemine başarıyla kayıt oldunuz! 🎉
                    
                    Artık sistem üzerinde size atanan görevleri görüntüleyebilir, araçlarınızı takip edebilir ve sürüş geçmişinize erişebilirsiniz.
                    
                    Hesap bilgilerinizle giriş yaparak aşağıdaki işlemleri gerçekleştirebilirsiniz:
                    • Sürüş görevlerinizi kontrol edin  
                    • Araç bakım ve durum bilgilerini görüntüleyin  
                    • Lisans belgelerinizi yönetin
                    
                    🔐 Güvenliğiniz için lütfen ilk girişte şifrenizi değiştirin.
                    
                    Herhangi bir sorunuz olması durumunda bizimle iletişime geçmekten çekinmeyin.
                    
                    İyi yolculuklar dileriz!  
                    FleetWise Ekibi
                    """, p.getUsername());

            mailService.sendMail(p.getEmail(), subject, body);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @KafkaListener(topics = "license-expiry-warning", groupId = "notification-group")
    public void getLicenseExpiryWarning(String message) throws Exception {
        try {
            DriverViewModel driverViewModel = objectMapper.readValue(message, DriverViewModel.class);
            PersonViewModel p = IUserClient.findById(driverViewModel.getPersonId());
            String subject = "⏰ Dikkat: Ehliyet Süreniz Yakında Doluyor!";
            String body = String.format("""
                    Merhaba %s,
                    
                    Sürücü sistemimizde kayıtlı olan ehliyetinizin süresi yakında sona erecek.
                    
                    Güvenli ve kesintisiz bir sürüş deneyimi için lütfen en kısa sürede ehliyetinizi yenileyin. Süresi dolmuş belgeler sistem üzerinde sürüş yetkinizi kısıtlayabilir.
                    
                    Yapabilecekleriniz:
                    • Ehliyet süresini kontrol edin  
                    • Yeni belgeyi sisteme yükleyin  
                    • Gerekirse yöneticinizle iletişime geçin
                    
                    📌 Bilgilerinizi güncel tutmanız, sistemden tam verim almanız açısından çok önemlidir.
                    
                    Teşekkür eder, güvenli sürüşler dileriz.  
                    FleetWise Ekibi
                    """, p.getUsername());

            mailService.sendMail(p.getEmail(), subject, body);
        } catch (Exception e) {
            throw new Exception(e);

        }
    }

    @KafkaListener(topics = "suspended-driver", groupId = "notification-group")
    public void getSuspendedDriver(String message) throws Exception {
        try {
            DriverViewModel driverViewModel = objectMapper.readValue(message, DriverViewModel.class);
            PersonViewModel p = IUserClient.findById(driverViewModel.getPersonId());
            String subject = "⚠️ Önemli: Sürücü Hesabınız Askıya Alındı";
            String body = String.format("""
                    Merhaba %s,
                    
                    FleetWise sistemi üzerinden yapılan değerlendirmeler sonucunda sürücü hesabınız geçici olarak askıya alınmıştır.
                    
                    Bu askıya alma işlemi aşağıdaki nedenlerden biriyle ilgili olabilir:
                    • Belge eksikliği  
                    • Kural ihlali  
                    • Süresi dolmuş lisans veya evrak
                    
                    ❗️Lütfen yöneticinizle iletişime geçerek detaylı bilgi alınız ve gerekli düzeltmeleri yapınız.
                    
                    Hesabınız tekrar aktif hale getirildiğinde tarafınıza bilgi verilecektir.
                    
                    Anlayışınız için teşekkür ederiz.  
                    FleetWise Güvenlik Ekibi
                    """, p.getUsername());

            mailService.sendMail(p.getEmail(), subject, body);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

}
