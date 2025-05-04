package com.example.student.service;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import java.util.Base64;
import io.jsonwebtoken.Claims;
import java.util.function.Function;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;


import org.springframework.stereotype.Service;

import com.example.student.Entity.StudentDisplayDetails;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTService {


    private String secretkey = "";

     public JWTService() {

        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keyGen.generateKey();
            secretkey = Base64.getEncoder().encodeToString(sk.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateToken(String  emailId) {
    return Jwts.builder()
            .claim("email",emailId) // assuming username is the email
            .setSubject("CustomToken")
            .signWith(getKey())
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + 15 * 60 * 1000))
            .compact();
}

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretkey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractEmailId(String token) {
        System.out.println("coming into extractEmailId method");

        return extractClaim(token, claims -> claims.get("email", String.class));
    }

    
    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, StudentDisplayDetails userDetails) {
        final String userName = extractEmailId(token);
        return (userName.equals(((StudentDisplayDetails) userDetails).getEmailId()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
    
