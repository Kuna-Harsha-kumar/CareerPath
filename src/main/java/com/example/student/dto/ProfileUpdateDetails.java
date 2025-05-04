package com.example.student.dto;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileUpdateDetails {
 
    

    
    @JsonIgnore
    public int studentId;
    public String studentName;
    public String emailid;
    public String contactNumber;
    public String skills;
    public String jobs;
    public Timestamp createdAt;
}

