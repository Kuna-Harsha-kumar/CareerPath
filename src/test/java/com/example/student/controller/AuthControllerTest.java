package com.example.student.controller;

import com.example.student.Entity.StudentDisplayDetails;
import com.example.student.dto.StudentDetails;
import com.example.student.service.userService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private userService userService;

    @InjectMocks
    private AuthController authController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    @DisplayName("GET /signin - Should return 200 and user details when successful")
    void signin_ShouldReturn200_WhenSuccessful() throws Exception {
        Map<String, StudentDisplayDetails> serviceResponse = new HashMap<>();
        serviceResponse.put("mock-token", new StudentDisplayDetails());

        when(userService.authenticateByEmail("test@example.com", "password")).thenReturn(serviceResponse);

        mockMvc.perform(get("/signin")
                .param("emailId", "test@example.com")
                .param("password", "password"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['mock-token']").exists());
    }

    @Test
    @DisplayName("GET /signin - Should return 404 when user not found (service returns null)")
    void signin_ShouldReturn404_WhenUserNotFound() throws Exception {
        when(userService.authenticateByEmail(anyString(), anyString())).thenReturn(null);

        mockMvc.perform(get("/signin")
                .param("emailId", "notfound@example.com")
                .param("password", "password"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.['Response : ']").value("User not found"));
    }

    @Test
    @DisplayName("POST /register - Should return 200 when successful")
    void register_ShouldReturn200_WhenSuccessful() throws Exception {
        when(userService.insertUserDetails(any(StudentDetails.class))).thenReturn("User addedd successfully");

        StudentDetails details = new StudentDetails();
        details.setEmailid("test@example.com");
        details.setPassword("password");

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(details)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Student registered successfully"));
    }

    @Test
    @DisplayName("POST /register - Should return 400 when registration fails")
    void register_ShouldReturn400_WhenFails() throws Exception {
        when(userService.insertUserDetails(any(StudentDetails.class))).thenReturn("Some error");

        StudentDetails details = new StudentDetails();
        details.setEmailid("test@example.com");
        details.setPassword("password");

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(details)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Some error"));
    }
}
