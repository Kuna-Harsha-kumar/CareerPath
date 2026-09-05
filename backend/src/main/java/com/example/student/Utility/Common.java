package com.example.student.Utility;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.example.student.dto.otpData;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class Common {

    private String secretkey = "";

    private final Random random = new Random();

      private final Map<String, otpData> otpStorage = new HashMap<>();

    public int getRandomThreeDigitNumber() {
        return 100 + random.nextInt(900); // 100 to 999 inclusive
    }

    public String generateOtp(String email) {
        String otp = String.valueOf(100000 + new Random().nextInt(900000));
        otpData otpdata=new otpData();
        otpdata.setOtp(otp);
        otpdata.setExpiryTime(LocalDateTime.now().plusMinutes(5));
        otpStorage.put(email, otpdata);
        return otp;
    }

    public boolean validateOtp(String email, String otpInput) {
        otpData otpData = otpStorage.get(email);
        if (otpData == null || LocalDateTime.now().isAfter(otpData.expiryTime)) {
            return false;
        }
        return otpData.otp.equals(otpInput);
    }

     public String extractEmailId(String token) {
        System.out.println("coming into extractEmailId method");
        return extractClaim(token, claims -> claims.get("email", String.class));
    }

    
    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }
    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretkey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    
}


