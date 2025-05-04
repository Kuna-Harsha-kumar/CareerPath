package com.example.student.Entity;


import lombok.Data;

@Data
public class StudentMeetingDetails {

    public int meeting_id;
    public String student_name;
    public int counselor_id;
    public String description;
    public String time;
    public String counselor_name;
    public int student_id;
    
}
