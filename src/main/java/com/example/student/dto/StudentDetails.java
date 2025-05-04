package com.example.student.dto;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StudentDetails {
    
    @JsonIgnore
    public int studentId;
    public String studentName;
    public String emailid;
    public String contactNumber;
    @JsonIgnore
    public String skills;
    @JsonIgnore
    public String experience;
    public String password;
    public Timestamp createdAt;
}
