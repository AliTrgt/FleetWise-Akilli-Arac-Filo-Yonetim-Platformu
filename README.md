# 🚗 FleetWise-Akilli-Arac-Filo-Yonetim-Platformu

FleetWise, filo yönetimi sistemleri için geliştirilmiş mikroservis tabanlı, ölçeklenebilir bir yazılım platformudur. Sürücü yönetimi, araç takibi, bakım süreçleri ve bildirim sistemlerini kapsayan servislerle entegre, modern bir altyapı sunar.

---

## 🧩 Mikroservisler

| Service              | Açıklama |
|----------------------|----------|
| **ApiGateway**       | Tüm servisler için tek giriş noktasıdır. CORS ve route yönetimi burada sağlanır. |
| **AuthService**      | JWT tabanlı kimlik doğrulama ve yetkilendirme servisi. |
| **DriverService**    | Sürücü yönetimi, kayıt, güncelleme, listeleme işlemlerini içerir. |
| **VehicleService**   | Araç bilgilerini tutar ve araç ile ilgili işlemleri yönetir. |
| **TripService**      | Yolculuk verilerini işler, sürücü ve araç eşleşmelerini yönetir. |
| **MaintenanceService** | Bakım geçmişi ve planlamalarını yönetir. |
| **NotificationService** | Kafka dinleyicileri ile uyarı ve bildirim sistemini yürütür. |
| **EurekaServer**     | Servis keşfi ve register işlemleri için Eureka sunucusu. |

---

## 🛠️ Kullanılan Teknolojiler

- 🧩 **Spring Boot** (Her bir mikroservis için)
- ☁️ **Spring Cloud** (Eureka, Config, Gateway)
- 🔐 **Spring Security + JWT**
- 📡 **Apache Kafka** (Asenkron bildirim ve event streaming)
- 💾 **Couchbase** (Veri deposu, MongoDB yerine)
- 🐳 **Docker & Docker Compose**
- 🔄 **FeignClient** ile servisler arası haberleşme

---

## 🚀 Kurulum

> Bu proje Docker ile kolayca ayağa kaldırılabilir.

### Gereksinimler

- Docker
- Docker Compose

### Kurulum Adımları

```bash
git clone https://github.com/kullanici-adi/fleetwise.git
cd fleetwise
docker-compose up --build
```

Tüm servisler aşağıdaki portlara göre çalışacaktır:

| Servis            | Port |
|------------------|------|
| ApiGateway        | 8080 |
| EurekaServer      | 8761 |
| AuthService       | 9001 |
| DriverService     | 9002 |
| VehicleService    | 9003 |
| TripService       | 9004 |
| MaintenanceService| 9005 |
| NotificationService | 9006 |

> Swagger UI adresleri için `ApiGateway` altında route'lara göz atabilirsiniz.

---

## 🔐 Kullanıcı Rolleri

- **Admin** – Tüm verilere erişebilir, yönetimsel yetkiler.
- **Operator** – Sürücü ve araç yönetimi yapabilir.
- **Viewer** – Salt okunur veri erişimi.

---

## 📡 Event Sistemi (Kafka)

Platform, mikroservisler arası iletişimi **Kafka** üzerinden yürütmektedir. Her önemli olay için ayrı topic tanımlanmış ve bu sayede servisler arasında loosely coupled bir yapı kurulmuştur.

### Kafka Topic Listesi

| Kafka Topic               | Açıklama                                                                 |
|--------------------------|--------------------------------------------------------------------------|
| `driver-created`         | Yeni sürücü oluşturulduğunda tetiklenir. Kullanıcıya hoş geldin e-postası gönderilir. |
| `license-expiry-warning` | Ehliyet süresi yaklaşan sürücüler için bilgilendirme yapılır.            |
| `suspended-driver`       | Askıya alınan sürücülere uyarı mesajı gider.                             |
| `trip-created`           | Yeni bir yolculuk oluşturulduğunda bilgilendirme yapılır.                |
| `trip-start`             | Yolculuk başlatıldığında e-posta gönderilir.                             |
| `trip-paused`            | Devam eden bir yolculuk duraklatıldığında kullanıcı bilgilendirilir.     |
| `trip-resume`            | Duraklatılan yolculuk devam ettirildiğinde kullanıcı haberdar edilir.    |
| `trip-cancel`            | İptal edilen yolculuk hakkında sürücüye e-posta gönderilir.              |
| `trip-complete`          | Tamamlanan yolculuk sonrası geri bildirim için bilgilendirme yapılır.    |
| `assign-driver-vehicle`  | Sürücüye yeni araç atandığında bildirim yapılır.                         |
| `unAssign-driver-vehicle`| Araç sürücüden kaldırıldığında işlem loglanır (şu an pasif).              |

### Avantajları:

- 🔄 Asenkron iletişim ile yüksek performans
- 🧩 Mikroservislerin birbirine bağımlılığını azaltır
- 📈 Gerçek zamanlı veri işleme ve izleme imkanı

> Kafka ile Event-Driven Architecture (EDA) benimsenerek daha sağlam ve esnek bir mikroservis yapısı sağlanmıştır.

## 📬 İletişim

Bu proje [Ali Turgut](https://github.com/AliTrgt) tarafından geliştirilmiştir.  
Her türlü katkı ve öneri için iletişime geçebilirsiniz.

