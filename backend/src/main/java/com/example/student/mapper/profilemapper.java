package com.example.student.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.student.Entity.StudentDisplayDetails;
import com.example.student.dto.ProfileUpdateDetails;
import com.example.student.dto.mentorDetials;

@Mapper
public interface profilemapper {

    @Update("UPDATE students SET skills = #{skills}, jobs=#{jobs} WHERE emailId = #{emailId}")
    void updateSkills(@Param("skills") String skills,@Param("jobs") String jobs,String emailId);

    @Select("Select * from Students where emailId=#{emailId}")
    List<StudentDisplayDetails> getStudentDetails(String emailId);

    @Select("Select * from counselors where counselor_id=#{counselor_id}")
    mentorDetials getCounselorDetails(int counselor_id);

    @Select("Select * from counselors where emailId=#{emailId}")
    mentorDetials getCounselorDetailsByEmailId(String emailId);

    @Update("update students set password=#{password} where emailId=#{emailId}")
    void updatePassword(String password,String emailId);

    @Update("update counselors set password=#{password} where emailId=#{emailId}")
    void updateCounselorPassword(String password,String emailId);

    @Update("UPDATE students SET " +
        "student_name = #{studentName}, " +
        "emailid = #{emailid}, " +
        "contact_number = #{contactNumber}, " +
        "skills = #{skills}, " +
        "jobs = #{jobs} "+
        "WHERE student_id = #{studentId}")
int updateUserDetails(ProfileUpdateDetails userDetails);

    
}
