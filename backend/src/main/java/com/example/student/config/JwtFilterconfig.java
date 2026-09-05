package com.example.student.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.student.Entity.MentorDisplayDetails;
import com.example.student.Entity.StudentDisplayDetails;
import com.example.student.mapper.userMapper;
import com.example.student.service.JWTService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilterconfig  extends OncePerRequestFilter{

     @Autowired
    private JWTService jwtService;

    @Autowired
    ApplicationContext context;

     @Autowired
    userMapper userMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    
        StudentDisplayDetails userDetails = new StudentDisplayDetails();
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String emailId = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            System.out.println("coming into this method");
            token = authHeader.substring(7);
            emailId = jwtService.extractEmailId(token);
        }

        if (emailId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            userDetails = userMapper.userDetails(emailId);
            System.out.println(emailId);
            if (userDetails!=null){
            if (jwtService.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, null);
                authToken.setDetails(new WebAuthenticationDetailsSource()
                        .buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

        }
            else {
                MentorDisplayDetails userDetails1 = userMapper.mentordetails(emailId);
                if (userDetails1 != null) {
                    userDetails = new StudentDisplayDetails();
                    userDetails.setContact_number(userDetails1.getContact_number());
                    userDetails.setEmailId(userDetails1.getEmailId());
                    userDetails.setStudent_id(userDetails1.getCounselor_id());
                    userDetails.setStudent_name(userDetails1.getCounselor_name());
                    userDetails.setPassword(userDetails1.getPassword());
                    userDetails.setSkills(userDetails1.getSkills());
                }
            }
            System.out.println(userDetails);
            if (jwtService.validateToken(token, userDetails)) {
                System.out.println("coming into this validation");
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, null);
                authToken.setDetails(new WebAuthenticationDetailsSource()
                        .buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }


        filterChain.doFilter(request, response);
    }
    
}
