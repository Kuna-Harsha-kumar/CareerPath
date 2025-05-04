package com.example.student.dto;

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
public class mentorDetials{


    public int counselor_id;
    public String counselor_name;
    public String emailId;
    public String contact_number;
    public String password;
    public String description;
    public String skills;
    public String experience;
}
