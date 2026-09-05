package com.example.student.controller;

import com.example.student.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
@CrossOrigin(origins = "http://localhost:5173")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send-otp")
    public String sendOtp(@RequestParam String email) {
        emailService.sendOtp(email);
        return "OTP sent to " + email;
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam String email, @RequestParam String otp) {
        boolean isValid = emailService.validateOtp(email, otp);
        return isValid ? "OTP Verified!" : "Invalid OTP!";
    }
}
