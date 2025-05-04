package com.example.student.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class MentorDisplayDetails {


    
    public int counselor_id;
    public String counselor_name;
    public String emailId;
    public String contact_number;
    public String description;
    public String skills;
    public String experience;
    @JsonIgnore
    public String password;
}
