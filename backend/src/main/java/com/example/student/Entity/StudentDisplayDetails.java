package com.example.student.Entity;



import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class StudentDisplayDetails{
    

    public Integer student_id;
    public String student_name;
    public String emailId;
    public String contact_number;
    public String skills;
    public String jobs;
    @JsonIgnore
    public String password;

    }
