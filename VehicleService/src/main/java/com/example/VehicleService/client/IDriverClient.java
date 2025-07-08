package com.example.VehicleService.client;

import com.example.VehicleService.dto.driverclient.DriverViewModel;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.UUID;


@FeignClient(name = "driver-service", path = "/api/driver")
public interface IDriverClient {

    @GetMapping("/me")
    @Retry(name = "driverRetry", fallbackMethod = "myRetry")
    @CircuitBreaker(name = "driverCircuitBreaker", fallbackMethod = "myCircuit")
    DriverViewModel me(@RequestHeader("Authorization") String authHeader);

    @GetMapping("/{driverId}")
    DriverViewModel findById(@PathVariable UUID driverId);


    default DriverViewModel myRetry(String authHeader, Throwable throwable) {
        LoggerFactory.getLogger(IDriverClient.class)
                .error("Retry Fallback : {}", throwable.getMessage());

        throw new RuntimeException("DriverService is unavailable (retry fallback)", throwable);
    }

    default DriverViewModel myCircuit(String authHeader, Throwable throwable) {
        LoggerFactory.getLogger(IDriverClient.class)
                .error("Circuit Fallback : {}", throwable.getMessage());

        throw new RuntimeException("DriverService is unavailable (circuit breaker)", throwable);
    }

}
