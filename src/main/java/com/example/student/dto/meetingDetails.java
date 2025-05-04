package com.example.student.dto;


import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;


@Data
public class meetingDetails {

    @JsonIgnore
    public int meeting_id;
    public String counselor_name;
    public int student_id;
    public int counselor_id;
    public String student_name;
    public String time;
    public String description;
    public Timestamp created_at;
    @JsonIgnore
    public String meeting_status;
    
}
