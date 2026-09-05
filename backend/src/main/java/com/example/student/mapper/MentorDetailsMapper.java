package com.example.student.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import com.example.student.Entity.MeetingDisplayDetails;
import com.example.student.Entity.MentorDisplayDetails;
import com.example.student.dto.meetingDetails;
import com.example.student.dto.mentorDetials;

@Mapper
public interface MentorDetailsMapper {
    
    @Select("select * from counselors")
    List<mentorDetials> getAllCounselors();

    @Select ("select * from Mentor_available_timings where counselor_id=#{counselor_id}")
    List<MeetingDisplayDetails> getAvailableTimings(int counselor_id);

     @Select("select * from student_meetings sm join students s on sm.student_id=s.student_id where counselor_id=#{counselor_id}")
    List<meetingDetails> getCouncelorStudents(int counselor_id);

    @Select("select * from counselors where emailid=#{emailId}")
    MentorDisplayDetails getMentorDetaisByEmailId(String emailId);

    @Select("Select * from counselors where counselor_id=#{counselor_id}")
    MentorDisplayDetails getMentorDetailsById(int counselor_id);

    @Insert("insert into counselors(counselor_id,counselor_name,emailId,contact_number,password,created_at,skills,experience) " +
        "values (#{counselor_id}, #{counselor_name},#{emailId} ,#{contact_number},#{password} , CURRENT_TIMESTAMP,#{skills},#{experience})")
    @Options(useGeneratedKeys = false)
    void insertUserDetails(mentorDetials mentorDetails);
}
