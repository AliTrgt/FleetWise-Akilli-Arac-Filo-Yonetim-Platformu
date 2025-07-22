package com.example.NotificationService.client;

import com.example.NotificationService.dto.DriverViewModel;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "driver-service",path = "/api/driver")
public interface IDriverClient {

    @GetMapping("/{driverId}")
    @CircuitBreaker(name = "findByIdCircuitBreaker",fallbackMethod = "findByIdError")
    @Retry(name = "findByIdRetry",fallbackMethod = "findByIdRetryError")
    DriverViewModel findById(@PathVariable UUID driverId);


    default DriverViewModel myCircuit(Throwable throwable) {
        LoggerFactory.getLogger(IDriverClient.class)
                .error("Circuit Fallback : {}", throwable.getMessage());

        throw new RuntimeException("findById is unavailable (circuit breaker)", throwable);
    }

    default DriverViewModel myRetry(Throwable throwable){
            LoggerFactory.getLogger(IDriverClient.class)
                    .error("Retry Fallback : {} ",throwable.getMessage());

        throw new RuntimeException("DriverService is unavailable (circuit breaker)", throwable);
    }


}
