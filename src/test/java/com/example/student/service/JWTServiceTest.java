package com.example.student.service;

import com.example.student.Entity.StudentDisplayDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JWTServiceTest {

    private JWTService jwtService;
    private final String TEST_EMAIL = "test@example.com";

    @BeforeEach
    void setUp() {
        jwtService = new JWTService();
    }

    @Test
    @DisplayName("Should generate a valid JWT token")
    void generateToken_ShouldReturnNotNullToken() {
        String token = jwtService.generateToken(TEST_EMAIL);
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    @DisplayName("Should extract correct email from token")
    void extractEmailId_ShouldReturnCorrectEmail() {
        String token = jwtService.generateToken(TEST_EMAIL);
        String extractedEmail = jwtService.extractEmailId(token);
        assertEquals(TEST_EMAIL, extractedEmail);
    }

    @Test
    @DisplayName("Should validate token correctly")
    void validateToken_ShouldReturnTrueForValidToken() {
        String token = jwtService.generateToken(TEST_EMAIL);
        StudentDisplayDetails userDetails = new StudentDisplayDetails();
        userDetails.setEmailId(TEST_EMAIL);

        assertTrue(jwtService.validateToken(token, userDetails));
    }

    @Test
    @DisplayName("Should return false when email does not match")
    void validateToken_ShouldReturnFalseForMismatchingEmail() {
        String token = jwtService.generateToken(TEST_EMAIL);
        StudentDisplayDetails userDetails = new StudentDisplayDetails();
        userDetails.setEmailId("other@example.com");

        assertFalse(jwtService.validateToken(token, userDetails));
    }
}
