package com.example.student.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.student.Entity.MeetingDisplayDetails;
import com.example.student.Entity.MentorDisplayDetails;
import com.example.student.dto.meetingDetails;
import com.example.student.dto.mentorDetials;
import com.example.student.service.counselorService;





@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class mentorController {


    @Autowired
    counselorService counselorDetials;


    @PostMapping("/MentorRegister")
     public ResponseEntity<?> register(@RequestBody mentorDetials userDetails) throws IOException {
        System.out.println(userDetails);
        System.out.println("coming into the controller");
        String response=counselorDetials.insertMentorDetails(userDetails);
        Map<String, String> responseBody = new HashMap<>();
        if ("User addedd successfully".equalsIgnoreCase(response)) {
            responseBody.put("message", "Student registered successfully");
            return ResponseEntity.status(200).body(responseBody);
        } else {
            responseBody.put("error", response);
            return ResponseEntity.status(400).body(responseBody);
        }
    }

   @GetMapping("/MentorSignin")
   public ResponseEntity<?> signin(@RequestParam String emailId,@RequestParam String password){
        Map<String,MentorDisplayDetails> userDetails=counselorDetials.authenticateMentorByEmail(emailId, password);
        System.out.println(userDetails);
        System.out.println(emailId);
        if(userDetails==null){
            Map<String,String> failureResponse = new HashMap<>();
            failureResponse.put("Response : ", "User not found");
            return ResponseEntity.status(404).body(failureResponse);
        }
        return ResponseEntity.status(200).body(userDetails);
   }

    @GetMapping("/mentorDetails")
    public ResponseEntity<?> getAllMentorDetails() {
        Map<String,List<mentorDetials>> successResponse = new HashMap<>();
        List<mentorDetials> mentorDetials=counselorDetials.geListoftAllCounselors();
        successResponse.put("Mentor Details", mentorDetials);
        return ResponseEntity.status(200).body(successResponse);
    }

    @GetMapping("/mentorAvailableTimings")
    public ResponseEntity<?> mentorAvailableTimings(int counselor_id) {
        Map<String,List<MeetingDisplayDetails>> response = new HashMap<>();
        List<MeetingDisplayDetails> mentorDetials=counselorDetials.getAvailableTimingDetails(counselor_id);
        if(mentorDetials!=null){
        response.put("Mentor Availability Details", mentorDetials);
        return ResponseEntity.status(200).body(response);
    }
    else{
        Map<String,String>  failureresponse = new HashMap<>();
        failureresponse.put("Mentor Not available","for counselling" );
        return ResponseEntity.status(200).body(failureresponse);
    }

    }

    @GetMapping("/getMentorStudents")
    public ResponseEntity<?> getMentorStudents(int counselor_id) {
        Map<String,List<meetingDetails>> response = new HashMap<>();
        System.out.println("mentorid"+counselor_id);
        List<meetingDetails> mentorDetials=counselorDetials.getStudentsofMentor(counselor_id);
        if(mentorDetials!=null){
        response.put("Mentor meeting Details", mentorDetials);
        return ResponseEntity.status(200).body(response);
    }
    else{
        Map<String,String>  failureresponse = new HashMap<>();
        failureresponse.put("Mentor Not available","for counselling" );
        return ResponseEntity.status(200).body(failureresponse);
    }
    }
}
