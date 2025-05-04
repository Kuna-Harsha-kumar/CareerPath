package com.example.student.service;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.student.Entity.StudentDisplayDetails;
import com.example.student.Utility.Common;
import com.example.student.dto.StudentDetails;
import com.example.student.mapper.userMapper;



@Service
public class userService implements UserDetailsService{

    @Autowired
    userMapper userMapper;

    @Autowired
    JWTService jwtService;

    @Autowired
    Common common;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();



    public String insertUserDetails(StudentDetails userDetails){
        StudentDisplayDetails userDetails2=userMapper.userDetails(userDetails.getEmailid());
        System.out.println(userDetails);
        if(userDetails2==null){
        int studentid=common.getRandomThreeDigitNumber();
        userDetails.setStudentId(studentid);
        userDetails.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        System.out.println(userDetails);
        userMapper.insertUserDetails(userDetails);
        }else{
            return "success";
        }
        return "User addedd successfully";
    }


    public Map<String,StudentDisplayDetails> authenticateByEmail(String email, String password) {
        Map<String,StudentDisplayDetails> response = new HashMap<>();
        String token=jwtService.generateToken(email);
        
        StudentDisplayDetails user = userMapper.userDetails(email);
        if (user==null){
            System.out.println("coming into user not there error");
            StudentDisplayDetails user1 = null;
            Map<String,StudentDisplayDetails> failureresponse = new HashMap<>();
            failureresponse.put("User not found",user1);
            return failureresponse;
        }if (user != null && passwordEncoder.matches(password, user.getPassword()) ) {
            System.out.println("coming here into user existing conditon");
            response.put(token, user);
            return response;
        }else if(!passwordEncoder.matches(password, user.getPassword())) {
            StudentDisplayDetails user1 = null;
            Map<String,StudentDisplayDetails> failureresponse = new HashMap<>();
            failureresponse.put("Password Incorrect",user1);
            return failureresponse;
        }else {
             Map<String,StudentDisplayDetails> failureresponse = new HashMap<>();
            failureresponse.put("User not found",user);
            return failureresponse;
        }
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'loadUserByUsername'");
    }

   
    
}
