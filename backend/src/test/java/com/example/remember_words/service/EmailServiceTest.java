package com.example.remember_words.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
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

    private static final String EMAIL = "john@example.com";
    private static final String USERNAME = "john";
    private User user;

    @BeforeEach
    void setUp() {
        user = new User(USERNAME, "encodedPassword", EMAIL);
    }

    @Test
    void sendRegistrationEmail_shouldBuildAndSendCorrectMessage(){
        String rawPassword = "plainPassword123";

        emailService.sendRegistrationEmail(user, rawPassword);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());

        SimpleMailMessage sent = captor.getValue();

        assertThat(sent.getTo()).containsExactly(EMAIL);
        assertThat(sent.getSubject()).isEqualTo("Welcome to Remember Words!");
        assertThat(sent.getText())
                .contains("Hello " + USERNAME + "!")
                .contains("Username: " + USERNAME)
                .contains("Password: " + rawPassword);
    }

    @Test
    void sendPasswordResetEmail_shouldBuildAndSendCorrectMessage() {
        String resetLink = "http://localhost/reset?token=abc";

        emailService.sendPasswordResetEmail(user, resetLink);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());

        SimpleMailMessage sent = captor.getValue();

        assertThat(sent.getTo()).containsExactly(EMAIL);
        assertThat(sent.getSubject()).isEqualTo("Password Reset Request");
        assertThat(sent.getText())
                .contains("Hello " + USERNAME + "!")
                .contains(resetLink);
    }
}
