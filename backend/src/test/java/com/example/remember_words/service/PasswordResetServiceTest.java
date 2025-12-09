package com.example.remember_words.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.remember_words.entity.PasswordResetTokens;
import com.example.remember_words.entity.User;
import com.example.remember_words.repository.PasswordResetTokenRepository;
import com.example.remember_words.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class PasswordResetServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private PasswordResetTokenRepository tokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PasswordResetService passwordResetService;

    private static final String EMAIL = "john@example.com";
    private static final String USERNAME = "john";
    private User user;

    @BeforeEach
    void setUp() {
        user = new User(USERNAME, "encodedPassword", EMAIL);
    }

    @Test
    void createPasswordResetToken_shouldSaveTokenAndSendEmail() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        passwordResetService.createPasswordResetToken(EMAIL);

        ArgumentCaptor<PasswordResetTokens> tokenCaptor = ArgumentCaptor.forClass(PasswordResetTokens.class);
        verify(tokenRepository).save(tokenCaptor.capture());
        PasswordResetTokens saved = tokenCaptor.getValue();

        assertThat(saved.getUser()).isEqualTo(user);
        assertThat(saved.getToken()).isNotBlank();
        assertThat(saved.getExpiresAt()).isAfter(LocalDateTime.now());

        String expectedPrefix = "https://localhost::8080/reset-password?token=";
        verify(emailService).sendPasswordResetEmail(eq(user),
                argThat(link -> link.startsWith(expectedPrefix)));
    }

    @Test
    void resetPassword_shouldEncodePasswordMarkTokenUsedAndPersist() {
        String token = "reset-token";
        PasswordResetTokens resetToken = new PasswordResetTokens(
                token,
                user,
                LocalDateTime.now().plusMinutes(30)
        );

        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(resetToken));
        when(passwordEncoder.encode("newPass")).thenReturn("encodedNewPass");

        passwordResetService.resetPassword(token, "newPass");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        assertThat(userCaptor.getValue().getPassword()).isEqualTo("encodedNewPass");

        ArgumentCaptor<PasswordResetTokens> tokenCaptor = ArgumentCaptor.forClass(PasswordResetTokens.class);
        verify(tokenRepository).save(tokenCaptor.capture());
        assertThat(tokenCaptor.getValue().isUsed()).isTrue();
    }
}
