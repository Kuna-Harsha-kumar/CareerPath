package com.example.student.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    private final String TEST_EMAIL = "test@example.com";

    @Test
    @DisplayName("Should send OTP and return it")
    void sendOtp_ShouldSendEmailAndReturnOtp() {
        String result = emailService.sendOtp(TEST_EMAIL);

        assertNotNull(result);
        assertEquals(6, result.length());
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    @DisplayName("Should validate OTP correctly")
    void validateOtp_ShouldReturnTrueForCorrectOtp() {
        String otp = emailService.sendOtp(TEST_EMAIL);

        assertTrue(emailService.validateOtp(TEST_EMAIL, otp));
        assertFalse(emailService.validateOtp(TEST_EMAIL, "wrong-otp"));
    }

    @Test
    @DisplayName("Should return false for non-existent email")
    void validateOtp_ShouldReturnFalseForUnknownEmail() {
        assertFalse(emailService.validateOtp("unknown@example.com", "123456"));
    }
}
