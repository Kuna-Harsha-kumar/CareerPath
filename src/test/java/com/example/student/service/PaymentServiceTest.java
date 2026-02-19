package com.example.student.service;

import com.example.student.dto.paymentDetails;
import com.example.student.dto.paymentResponse;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @InjectMocks
    private paymentService paymentService;

    private MockedStatic<Session> mockedSession;

    @BeforeEach
    void setUp() {
        mockedSession = mockStatic(Session.class);
        // Inject the @Value field manually
        ReflectionTestUtils.setField(paymentService, "secretKey", "sk_test_mock");
    }

    @AfterEach
    void tearDown() {
        mockedSession.close();
    }

    @Test
    @DisplayName("Should create payment session successfully")
    void checkoutProducts_ShouldReturnSuccessResponse() throws Exception {
        paymentDetails request = new paymentDetails();
        request.setName("Test Product");
        request.setAmount(1000L);
        request.setCurrency("USD");

        Session mockSession = mock(Session.class);
        when(mockSession.getId()).thenReturn("sess_123");
        when(mockSession.getUrl()).thenReturn("http://stripe.com/pay");
        
        mockedSession.when(() -> Session.create(any(SessionCreateParams.class))).thenReturn(mockSession);

        paymentResponse response = paymentService.checkoutProducts(request);

        assertNotNull(response);
        assertEquals("SUCCESS", response.getStatus());
        assertEquals("sess_123", response.getSessionId());
        assertEquals("http://stripe.com/pay", response.getSessionUrl());
    }
}
