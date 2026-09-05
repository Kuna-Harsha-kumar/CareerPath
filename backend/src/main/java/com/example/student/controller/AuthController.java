package com.example.student.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.student.Entity.StudentDisplayDetails;
import com.example.student.dto.StudentDetails;
import com.example.student.service.userService;

@Controller
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController{
    
    @Autowired
    userService userService;


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody StudentDetails userDetails) throws IOException {
        String response = userService.insertUserDetails(userDetails);
        Map<String, String> responseBody = new HashMap<>();
        if ("User addedd successfully".equalsIgnoreCase(response)) {
            responseBody.put("message", "Student registered successfully");
            return ResponseEntity.status(200).body(responseBody);
        } else {
            responseBody.put("error", response);
            return ResponseEntity.status(400).body(responseBody);
        }
    }
    

   @GetMapping("/signin")
   public ResponseEntity<?> signin(@RequestParam String emailId,@RequestParam String password){
        Map<String,StudentDisplayDetails> userDetails=userService.authenticateByEmail(emailId, password);
        System.out.println(userDetails);
        if(userDetails==null){
            Map<String,String> failureResponse = new HashMap<>();
            failureResponse.put("Response : ", "User not found");
            return ResponseEntity.status(404).body(failureResponse);
        }
        return ResponseEntity.status(200).body(userDetails);
   }
}
