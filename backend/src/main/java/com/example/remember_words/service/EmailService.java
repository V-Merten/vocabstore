package com.example.remember_words.service;

import java.util.logging.Logger;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.remember_words.entity.User;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final Logger logger = Logger.getLogger(EmailService.class.getName());

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendRegistrationEmail(User user, String rawPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Welcome to Remember Words!");
        message.setText(
                "Hello " + user.getUsername() + "!\n\n" +
                "Your registration was successful.\n\n" +
                "Here are your details:\n" +
                "Username: " + user.getUsername() + "\n" +
                "Password: " + rawPassword + "\n\n" +
                "Welcome!"
            );

        mailSender.send(message);
        logger.info("Registration email sent to " + user.getEmail());
    }
    
}
