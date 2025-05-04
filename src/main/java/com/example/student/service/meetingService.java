package com.example.student.service;

import java.sql.Time;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.student.Entity.MentorDisplayDetails;
import com.example.student.Entity.StudentDisplayDetails;
import com.example.student.Entity.StudentMeetingDetails;
import com.example.student.Utility.Common;
import com.example.student.dto.meetingDetails;
import com.example.student.dto.mentorAvailability;
import com.example.student.mapper.MeetingMapper;
import com.example.student.mapper.MentorDetailsMapper;
import com.example.student.mapper.userMapper;

@Service
public class meetingService {

    @Autowired
    MeetingMapper meetingMapper;

    @Autowired
    Common common;

    @Autowired
    MentorDetailsMapper mentorDetailsMapper;

    // @Autowired
    // profileMapper 

    @Autowired
    userMapper userMapper;

    public String scheduleMeeting(String inputdate,int counselor_id,String description){
        MentorDisplayDetails mentorDetails=mentorDetailsMapper.getMentorDetailsById(counselor_id);
        String emailId = ((StudentDisplayDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmailId();
        StudentDisplayDetails details=userMapper.userDetails(emailId);
        meetingDetails meeting=meetingMapper.getMeetingDetails(details.getStudent_id(), mentorDetails.getCounselor_id(),inputdate);    
        meetingDetails meetingDetails=new meetingDetails();
        if(meeting==null){
            meetingDetails.setCounselor_id(counselor_id);
            meetingDetails.setStudent_id(details.getStudent_id());
            meetingDetails.setCounselor_name(mentorDetails.getCounselor_name());
            meetingDetails.setMeeting_id(common.getRandomThreeDigitNumber());
            meetingDetails.setMeeting_status("active");
            meetingDetails.setDescription(description);
            meetingDetails.setTime(inputdate);
        meetingMapper.createMeeting(meetingDetails);
        }else{
            return "Meeting already created";
        }
        return "Meeting created";
    }

    public List<StudentMeetingDetails> getAllStudentMeetingDetails(){
        String emailId = ((StudentDisplayDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmailId();
        StudentDisplayDetails details=userMapper.userDetails(emailId);
        List<StudentMeetingDetails> meetingDetails=meetingMapper.getStudentMeetingDetails(details.getStudent_id());
        return meetingDetails;
    }

    public void deleteMeeting(int meeting_id){
        System.out.println("deleting servicce");
        meetingMapper.deleteMeeting(meeting_id);
    }

    public void updateMeeting(int meeting_id,String time,String description){
        System.out.println("updating servicce");
        meetingMapper.deleteMeeting(meeting_id);
    }
    
     public Map<String,mentorAvailability> createMeetingByMentor(String day,Time time){
        String emailId = ((StudentDisplayDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmailId();
        mentorAvailability mentorAvailability=meetingMapper.checkMeetingAvailability(day, time);
        System.out.println(mentorAvailability);
        MentorDisplayDetails mentorDetails=mentorDetailsMapper.getMentorDetaisByEmailId(emailId);
        mentorAvailability createMeeting=new mentorAvailability();
        System.out.println("coming in front of if fucntion into the service class");
        if (mentorAvailability==null){
            createMeeting.setCounselor_id(mentorDetails.getCounselor_id());
            createMeeting.setTime(time);
            createMeeting.setAvailable_id(common.getRandomThreeDigitNumber());
            createMeeting.setDay(day);
            createMeeting.setStatus("Active");
            meetingMapper.insertAvailableTimings(createMeeting);
            System.out.println(createMeeting);
            Map<String,mentorAvailability> response=new HashMap<>();
            response.put("Available time updated", createMeeting);
            return response;
        }
    return null;
}
}
