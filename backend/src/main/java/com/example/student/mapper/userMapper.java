package com.example.student.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import com.example.student.Entity.MentorDisplayDetails;
import com.example.student.Entity.StudentDisplayDetails;
import com.example.student.dto.StudentDetails;

@Mapper
public interface userMapper {
    

    @Insert("insert into students(student_id,student_name,emailId,contact_number,password,created_at) " +
        "values (#{studentId}, #{studentName},#{emailid} ,#{contactNumber},#{password} , CURRENT_TIMESTAMP)")
    @Options(useGeneratedKeys = false)
    int insertUserDetails(StudentDetails userDetails);

    @Select("Select * from students where emailId=#{emailId}")
    StudentDisplayDetails userDetails(String emaildId);

    @Select("select * from counselors where emailid=#{emailId}")
    MentorDisplayDetails mentordetails(String emailId);

    // @Update("update ")
}
