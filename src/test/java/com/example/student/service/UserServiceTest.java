package com.example.student.service;

import com.example.student.Entity.StudentDisplayDetails;
import com.example.student.mapper.userMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserServiceTest {

    @Mock
    private userMapper userMapper;

    @Mock
    private JWTService jwtService;

    @InjectMocks
    private userService userService;

    // Use a real encoder to prepare test data, as the service uses its own internal final instance
    private final BCryptPasswordEncoder realEncoder = new BCryptPasswordEncoder();

    private final String VALID_EMAIL = "test@example.com";
    private final String VALID_PASSWORD = "password123";

    @Test
    @DisplayName("Should return 'User not found' map when user does not exist")
    void authenticateByEmail_ShouldReturnUserNotFound_WhenUserDoesNotExist() {
        when(jwtService.generateToken(anyString())).thenReturn("mock-token");
        when(userMapper.userDetails(anyString())).thenReturn(null);

        Map<String, StudentDisplayDetails> response = userService.authenticateByEmail(VALID_EMAIL, VALID_PASSWORD);

        assertNotNull(response);
        assertTrue(response.containsKey("User not found"));
    }

    @Test
    @DisplayName("Should return 'Password Incorrect' map when password does not match")
    void authenticateByEmail_ShouldReturnPasswordIncorrect_WhenPasswordIsWrong() {
        StudentDisplayDetails user = new StudentDisplayDetails();
        user.setEmailId(VALID_EMAIL);
        // Set a password that definitely won't match "password123"
        user.setPassword(realEncoder.encode("differentPassword"));

        when(jwtService.generateToken(anyString())).thenReturn("mock-token");
        when(userMapper.userDetails(anyString())).thenReturn(user);

        Map<String, StudentDisplayDetails> response = userService.authenticateByEmail(VALID_EMAIL, VALID_PASSWORD);

        assertNotNull(response);
        assertTrue(response.containsKey("Password Incorrect"));
    }

    @Test
    @DisplayName("Should return map with token and user details on successful authentication")
    void authenticateByEmail_ShouldReturnToken_WhenCredentialsAreValid() {
        StudentDisplayDetails user = new StudentDisplayDetails();
        user.setStudent_id(123);
        user.setEmailId(VALID_EMAIL);
        // Encode the VALID_PASSWORD so the service's internal encoder can match it
        user.setPassword(realEncoder.encode(VALID_PASSWORD));

        String mockToken = "mock-jwt-token";
        
        when(jwtService.generateToken(anyString())).thenReturn(mockToken);
        when(userMapper.userDetails(anyString())).thenReturn(user);

        Map<String, StudentDisplayDetails> response = userService.authenticateByEmail(VALID_EMAIL, VALID_PASSWORD);

        assertNotNull(response);
        assertTrue(response.containsKey(mockToken), "Expected " + mockToken + " but keys were " + response.keySet());
        assertEquals(user, response.get(mockToken));
    }
}
