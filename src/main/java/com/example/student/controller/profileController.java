package com.example.student.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.student.Entity.StudentDisplayDetails;
import com.example.student.Utility.Common;
import com.example.student.dto.ProfileUpdateDetails;
import com.example.student.dto.mentorDetials;
import com.example.student.service.profileService;




@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class profileController {


    @Autowired
    profileService profileService;

    @Autowired
    Common common;



    @GetMapping("/studentDetails")
    public ResponseEntity<?> getStudentDetails(){
        String email = ((StudentDisplayDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmailId();
        System.out.println("email"+email);
        List<StudentDisplayDetails> studentDetails=profileService.getStudentDetails(email);
        if(studentDetails.size()!=0){
            return ResponseEntity.status(200).body(studentDetails);
        }
        else{
            Map<String,String> failureResponse = new HashMap<>();
            failureResponse.put("Student Details", "not found");
            return ResponseEntity.status(404).body(failureResponse);
        }
    }

    @GetMapping("/counselorDetails")
    public ResponseEntity<?> getCounselorDetails(int counselor_id){
        String email = ((StudentDisplayDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmailId();
        System.out.println("coming into the controller after authentication");
        mentorDetials studentDetails=profileService.getCounselorDetails(counselor_id);
        System.out.println (studentDetails);
        if(studentDetails!=null){
            return ResponseEntity.status(200).body(studentDetails);
        }
        else{
            Map<String,String> failureResponse = new HashMap<>();
            failureResponse.put("Student Details", "not found");
            return ResponseEntity.status(404).body(failureResponse);
        }
    }
    @GetMapping("/counselorDetailswithEmailId")
    public ResponseEntity<?> getCounselorDetails(){
        String email = ((StudentDisplayDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmailId();
        System.out.println("coming into the controller after authentication");
        mentorDetials studentDetails=profileService.getCounselorDetailsByEmail(email);
        System.out.println (studentDetails);
        if(studentDetails!=null){
            return ResponseEntity.status(200).body(studentDetails);
        }
        else{
            Map<String,String> failureResponse = new HashMap<>();
            failureResponse.put("Student Details", "not found");
            return ResponseEntity.status(404).body(failureResponse);
        }
    }
    @PostMapping("/updatePassword")
    public ResponseEntity<?> updateStudentPassword(@RequestParam String emailId,@RequestParam String password) {
        profileService.updatePassword(password, emailId);
            return ResponseEntity.status(200).body("Password updated");
    }

    @PostMapping("/updateCounselorPassword")
    public ResponseEntity<?> updateCounselorPassword(@RequestParam String emailId,@RequestParam String password) {
        System.out.println("ciming into backend");
        profileService.updateCounselotPassword(password, emailId);
            return ResponseEntity.status(200).body("Password updated");
    }
    
   @PostMapping("/updateProfile")
     public ResponseEntity<?> register(@RequestBody ProfileUpdateDetails userDetails) throws IOException {
        Map<String,String> successResponse = new HashMap<>();
        System.out.println(userDetails);

        String response=profileService.updateProfileDetails(userDetails);
        if (response!=null){
            successResponse.put("Student Details", response);
            return ResponseEntity.status(200).body(successResponse);
        }
        Map<String,String> failureResponse = new HashMap<>();
        failureResponse.put("Student Details", response);
        return ResponseEntity.status(400).body(failureResponse);
   }
    


}
