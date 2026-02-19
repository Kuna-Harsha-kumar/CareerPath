package com.example.student.service;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private final ConcurrentHashMap<String, String> otpStorage = new ConcurrentHashMap<>();

    public String sendOtp(String toEmail) {
        String otp = generateOtp();
        otpStorage.put(toEmail, otp);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your OTP Code");
        message.setText("Your OTP code is: " + otp);

        System.out.println(message);
        mailSender.send(message);
        return otp;
    }

    public boolean validateOtp(String email, String inputOtp) {
        String storedOtp = otpStorage.get(email);
        return storedOtp != null && storedOtp.equals(inputOtp);
    }

    private String generateOtp() {
        return String.format("%06d", new Random().nextInt(999999));
    }
}
