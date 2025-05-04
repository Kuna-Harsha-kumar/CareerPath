package com.example.student.Entity;

import java.sql.Time;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class MeetingDisplayDetails {

    @JsonIgnore
    public int available_id;
    public int counselor_id;
    public String day;
    public Time time;
    @JsonIgnore
    public Timestamp created_at;
    @JsonIgnore
    public Timestamp updated_at;
    
}
