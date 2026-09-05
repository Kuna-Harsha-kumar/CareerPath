package com.example.student.service;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.student.Entity.StudentDisplayDetails;
import com.example.student.dto.ProfileUpdateDetails;
import com.example.student.dto.mentorDetials;
import com.example.student.mapper.profilemapper;
import com.example.student.mapper.userMapper;


@Service
public class profileService {

    @Autowired
    public profilemapper profilemapper;

    @Autowired
    public userMapper userMapper;

     private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public void updateProfile(Map<String, Object> analysisResult){
        String email = ((StudentDisplayDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmailId();
        List<String> value = analysisResult.values()
        .stream()
        .map(Object::toString)
        .collect(Collectors.toList());
        String skills=value.get(2).toString();
        System.out.println(value);
        String jobs=value.get(1);
        profilemapper.updateSkills(skills,jobs,email);
    }

    public List<StudentDisplayDetails> getStudentDetails(String emailid){
        List<StudentDisplayDetails> studentDetails=profilemapper.getStudentDetails(emailid);
        return studentDetails;
    }

    public mentorDetials getCounselorDetails(int counselor_id){
        mentorDetials studentDetails=profilemapper.getCounselorDetails(counselor_id);
        return studentDetails;
    }

    public mentorDetials getCounselorDetailsByEmail(String emailId){
        mentorDetials studentDetails=profilemapper.getCounselorDetailsByEmailId(emailId);
        return studentDetails;
    }

    public void  updatePassword(String password,String emailId){
        profilemapper.updatePassword(passwordEncoder.encode(password), emailId);
    }

    public void  updateCounselotPassword(String password,String emailId){
        profilemapper.updateCounselorPassword(passwordEncoder.encode(password), emailId);
    }

public String updateProfileDetails(ProfileUpdateDetails userDetails){
    String email = ((StudentDisplayDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmailId();
        StudentDisplayDetails userDetails2=userMapper.userDetails(email);
        System.out.println(userDetails);
        if(userDetails2!=null){
            
            userDetails.setStudentId(userDetails2.getStudent_id());
        profilemapper.updateUserDetails(userDetails);
        return "User Details updated successfully";
        }
        return "User details updated successfully";
    }

    
}

