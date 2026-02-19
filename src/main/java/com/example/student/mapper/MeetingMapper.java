package com.example.student.mapper;

import java.sql.Time;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.student.Entity.StudentMeetingDetails;
import com.example.student.dto.meetingDetails;
import com.example.student.dto.mentorAvailability;

@Mapper
public interface MeetingMapper {
    

        @Select("select * from Mentor_available_timings where day=#{day} and time=#{time}")
        mentorAvailability checkMeetingAvailability(String day,Time time);

        @Insert("Insert into Mentor_available_timings(available_id,counselor_id,day,time,created_at,updated_at) "+
        "values (#{available_id},#{counselor_id},#{day},#{time},CURRENT_TIMESTAMP,CURRENT_TIMESTAMP)")
        @Options(useGeneratedKeys = false)
        void insertAvailableTimings(mentorAvailability MentorAvailability);

        @Select("SELECT * FROM student_meetings sm JOIN Students s ON sm.student_id = s.student_id WHERE sm.student_id = #{studentId} AND sm.meeting_status = 'Active'")
        List<StudentMeetingDetails> getStudentMeetingDetails(int studentId);


        @Select("select * from student_meetings where student_id=#{student_id} and counselor_id=#{counselor_id} and time=#{time} and meeting_status='active'")
        meetingDetails getMeetingDetails(int student_id,int counselor_id,String time);

        @Update("update student_meetings set meeting_status='Deactive' where meeting_id=#{meeting_id}")
        void deleteMeeting(int meeting_id);

        @Update("UPDATE student_meetings SET time = #{time}, description = #{description} WHERE meeting_id = #{meeting_id}")
        void updateMeeting(int meeting_id,String time,String description);


        @Insert("insert into student_meetings(meeting_id,counselor_id,student_id,time,description,created_at,meeting_status,counselor_name) " +
        "values(#{meeting_id},#{counselor_id},#{student_id},#{time},#{description}, CURRENT_TIMESTAMP,#{meeting_status},#{counselor_name})")
        @Options(useGeneratedKeys = false)
        void createMeeting(meetingDetails meetingDetails);
}
