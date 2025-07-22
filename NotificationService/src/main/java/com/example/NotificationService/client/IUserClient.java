package com.example.NotificationService.client;

import com.example.NotificationService.dto.DriverViewModel;
import com.example.NotificationService.dto.PersonViewModel;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service",path ="/api/person")
public interface IUserClient {

    @GetMapping("/{id}")
    @CircuitBreaker(name = "findByIdCircuitBreaker",fallbackMethod = "findByIdFallBack")
    @Retry(name = "IdRetry",fallbackMethod = "findByIdRetry")
    PersonViewModel findById(@PathVariable int id);


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
