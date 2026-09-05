package com.example.student.dto;

import java.sql.Time;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class mentorAvailability {

    public int available_id;
    public int counselor_id;
    public Time time;
    public String day;
    @JsonIgnore
    public Timestamp created_at;
    @JsonIgnore
    public Timestamp updated_at;
    @JsonIgnore
    public String status;


    
}
