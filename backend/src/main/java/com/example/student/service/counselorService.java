package com.example.student.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.student.Entity.MeetingDisplayDetails;
import com.example.student.Entity.MentorDisplayDetails;
import com.example.student.Utility.Common;
import com.example.student.dto.meetingDetails;
import com.example.student.dto.mentorDetials;
import com.example.student.mapper.MentorDetailsMapper;

@Service
public class counselorService {
    

    @Autowired
    MentorDetailsMapper mentorDetailsmapper;

    @Autowired
    Common common;


    @Autowired
    JWTService jwtService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public List<mentorDetials> geListoftAllCounselors(){
        List<mentorDetials> counselorDetials = mentorDetailsmapper.getAllCounselors();
        return counselorDetials;
    }

    public List<meetingDetails> getStudentsofMentor(int councelor_id){
        List<meetingDetails> counselorDetials = mentorDetailsmapper.getCouncelorStudents(councelor_id);
        return counselorDetials;
    }

    public List<MeetingDisplayDetails> getAvailableTimingDetails(int counselor_id){
        List<MeetingDisplayDetails> availableTimings=mentorDetailsmapper.getAvailableTimings(counselor_id);
        return availableTimings;
    }

     public String insertMentorDetails(mentorDetials mentorDetials){
        MentorDisplayDetails userDetails2=mentorDetailsmapper.getMentorDetaisByEmailId(mentorDetials.getEmailId());
        System.out.println("coming into the service");
        System.out.println(mentorDetials);
        if(userDetails2==null){
        int studentid=common.getRandomThreeDigitNumber();
        mentorDetials.setCounselor_id(studentid);
        mentorDetials.setPassword(passwordEncoder.encode(mentorDetials.getPassword()));
        System.out.println(mentorDetials);
        mentorDetailsmapper.insertUserDetails(mentorDetials);
        }else{
            return "Emailid already existing";
        }
        return "User addedd successfully";
    }


    public Map<String,MentorDisplayDetails> authenticateMentorByEmail(String email, String password) {
        Map<String,MentorDisplayDetails> response = new HashMap<>();
        String token=jwtService.generateToken(email);
        MentorDisplayDetails user = mentorDetailsmapper.getMentorDetaisByEmailId(email);
       System.out.println(user);
        if (user.getCounselor_id()==0){
            MentorDisplayDetails user1 = null;
            Map<String,MentorDisplayDetails> failureresponse = new HashMap<>();
            failureresponse.put("User not found",user1);
            return failureresponse;
        }
        System.out.println(passwordEncoder.matches(password, user.getPassword()));
        if (user!=null && passwordEncoder.matches(password, user.getPassword()) ) {
            System.out.println("should come here");
            response.put(token, user);
            return response;
        }else if(!passwordEncoder.matches(password, user.getPassword())) {
            MentorDisplayDetails user1 = null;
            Map<String,MentorDisplayDetails> failureresponse = new HashMap<>();
            failureresponse.put("Password Incorrect",user1);
            return failureresponse;
        }else {
             Map<String,MentorDisplayDetails> failureresponse = new HashMap<>();
            failureresponse.put("User not found for email ",user);
            return failureresponse;
        }
    }

}
