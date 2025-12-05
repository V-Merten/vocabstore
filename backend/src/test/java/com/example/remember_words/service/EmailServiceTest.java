package com.example.remember_words.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.example.remember_words.entity.User;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @Test
    void sendRegistrationEmail_shouldBuildAndSendCorrectMessage(){

        User user = new User("john", "encodedPassword", "john@example.com");
        String rawPassword = "plainPassword123";

        emailService.sendRegistrationEmail(user, rawPassword);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());

        SimpleMailMessage sent = captor.getValue();

        assertThat(sent.getTo()).containsExactly("john@example.com");
        assertThat(sent.getSubject()).isEqualTo("Welcome to Remember Words!");
        assertThat(sent.getText())
                .contains("Hello john!")
                .contains("Username: john")
                .contains("Password: plainPassword123");
    }

}
