package com.example.student.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class otpData {
    

    public String otp;
    public LocalDateTime expiryTime;

    void OtpData(String otp, LocalDateTime expiryTime) {
        this.otp = otp;
        this.expiryTime = expiryTime;
    }
}
