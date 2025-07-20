package com.example.NotificationService.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailDto {
    private String to;
    private String subject;
    private String body;
}
