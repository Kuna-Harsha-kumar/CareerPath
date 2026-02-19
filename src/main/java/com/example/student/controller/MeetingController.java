package com.example.student.controller;


import java.sql.Time;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.student.Entity.StudentMeetingDetails;
import com.example.student.dto.mentorAvailability;
import com.example.student.service.meetingService;

@RestController
public class MeetingController {

    @Autowired
    meetingService meetingService;
    

    @PostMapping("/studentScheduleMeeting")
    public ResponseEntity<?> scheduleMeeting(@RequestParam String time,@RequestParam int counselor_id,@RequestParam String description) {
    
    Map<String,String> successResponse = new HashMap<>();
    String details=meetingService.scheduleMeeting(time,counselor_id,description);
    
    if(details.equalsIgnoreCase("Meeting already created")){
        Map<String,String> failureResponse = new HashMap<>();
        failureResponse.put("Meeting cannot be added", details);
        return ResponseEntity.status(400).body(failureResponse);
    }if(details.equalsIgnoreCase("Meeting created")){
    return ResponseEntity.status(200).body(successResponse);
    }
    return ResponseEntity.status(200).body(successResponse);
}

 @PostMapping("/createAvailableTimings")
    public ResponseEntity<?> updateMentorAvailableTimings(@RequestParam Time time,@RequestParam String day) {
        Map<String,mentorAvailability> response = new HashMap<>();
        response=meetingService.createMeetingByMentor(day, time);
        if(response!=null){
        return ResponseEntity.status(200).body(response);
        }else{
            Map<String,String> failureresponse = new HashMap<>();
            failureresponse.put("Meeting already available", "New Meeting cant be added");
            return ResponseEntity.status(400).body(failureresponse);
        }
    }
    @GetMapping("/getMeetingDetailsOfStudent")
    public ResponseEntity<?> getAlltheStudentMeetings(){
        List<StudentMeetingDetails> meetingDetails=meetingService.getAllStudentMeetingDetails();
        if(meetingDetails==null){
            Map<String,String> failureresponse = new HashMap<>(); 
            failureresponse.put("No meetings ", "scheduled for the user");
            return ResponseEntity.status(400).body(failureresponse);
        }else{
            Map<String,List<StudentMeetingDetails>> successResponse = new HashMap<>(); 
            successResponse.put("Meeting Details", meetingDetails);
            return ResponseEntity.status(200).body(successResponse);
        }
    }

    @DeleteMapping("/deleteStudentMeeting")
    public ResponseEntity<?> deleteStudentMeeting(@RequestParam int meeting_id){
        System.out.println(meeting_id);
        System.out.println("coming into this methoid");
        meetingService.deleteMeeting(meeting_id);
        Map<String,String> successResponse = new HashMap<>(); 
        successResponse.put("Meeting has ", "been deleted");
        return ResponseEntity.status(200).body(successResponse);
        }

        @PostMapping("/updateMeeting")
        public ResponseEntity<?> updateExistingMeeting(@RequestParam int meeting_id,@RequestParam String time,@RequestParam String description){
            System.out.println(time);
            System.out.println(meeting_id);
            meetingService.updateMeeting(meeting_id, time, description);
            Map<String,String> successResponse = new HashMap<>(); 
            successResponse.put("Meeting has ", "been updated");
            return ResponseEntity.status(200).body(successResponse);
            }



}
