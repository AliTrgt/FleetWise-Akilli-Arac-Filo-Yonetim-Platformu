package com.example.NotificationService.job;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private final JavaMailSender mailSender;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendMail(String to,String subject,String body){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("alitrgttt@gmail.com");
        message.setSubject(subject);
        message.setTo(to);
        message.setText(body);

        mailSender.send(message);
    }


}
