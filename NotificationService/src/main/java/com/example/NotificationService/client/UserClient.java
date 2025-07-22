package com.example.NotificationService.client;

import com.example.NotificationService.dto.PersonViewModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service",path ="/api/person")
public interface UserClient {

    @GetMapping("/{id}")
    PersonViewModel findById(@PathVariable int id);

}
