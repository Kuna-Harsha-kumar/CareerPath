package com.example.student.service;

import com.example.student.Entity.MentorDisplayDetails;
import com.example.student.Entity.StudentDisplayDetails;
import com.example.student.Entity.StudentMeetingDetails;
import com.example.student.Utility.Common;
import com.example.student.dto.meetingDetails;
import com.example.student.dto.mentorAvailability;
import com.example.student.mapper.MeetingMapper;
import com.example.student.mapper.MentorDetailsMapper;
import com.example.student.mapper.userMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MeetingServiceTest {

    @Mock
    private MeetingMapper meetingMapper;

    @Mock
    private Common common;

    @Mock
    private MentorDetailsMapper mentorDetailsMapper;

    @Mock
    private userMapper userMapper;

    @InjectMocks
    private meetingService meetingService;

    private MockedStatic<SecurityContextHolder> mockedSecurityContextHolder;
    private final String TEST_EMAIL = "test@example.com";

    @BeforeEach
    void setUp() {
        mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class);
        
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        StudentDisplayDetails principal = new StudentDisplayDetails();
        principal.setEmailId(TEST_EMAIL);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(principal);
        mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
    }

    @AfterEach
    void tearDown() {
        mockedSecurityContextHolder.close();
    }

    @Test
    @DisplayName("Should schedule meeting successfully")
    void scheduleMeeting_ShouldReturnSuccess_WhenNewMeeting() {
        int counselorId = 1;
        MentorDisplayDetails mentor = new MentorDisplayDetails();
        mentor.setCounselor_id(counselorId);
        mentor.setCounselor_name("Mentor Name");

        StudentDisplayDetails student = new StudentDisplayDetails();
        student.setStudent_id(100);

        when(mentorDetailsMapper.getMentorDetailsById(counselorId)).thenReturn(mentor);
        when(userMapper.userDetails(TEST_EMAIL)).thenReturn(student);
        when(meetingMapper.getMeetingDetails(eq(100), eq(counselorId), anyString())).thenReturn(null);
        when(common.getRandomThreeDigitNumber()).thenReturn(999);

        String result = meetingService.scheduleMeeting("2023-10-10", counselorId, "Desc");

        assertEquals("Meeting created", result);
        verify(meetingMapper).createMeeting(any(meetingDetails.class));
    }

    @Test
    @DisplayName("Should return all meeting details for student")
    void getAllStudentMeetingDetails_ShouldReturnList() {
        StudentDisplayDetails student = new StudentDisplayDetails();
        student.setStudent_id(100);
        List<StudentMeetingDetails> list = new ArrayList<>();

        when(userMapper.userDetails(TEST_EMAIL)).thenReturn(student);
        when(meetingMapper.getStudentMeetingDetails(100)).thenReturn(list);

        List<StudentMeetingDetails> result = meetingService.getAllStudentMeetingDetails();
        assertEquals(list, result);
    }

    @Test
    @DisplayName("Should delete meeting")
    void deleteMeeting_ShouldCallMapper() {
        meetingService.deleteMeeting(1);
        verify(meetingMapper).deleteMeeting(1);
    }

    @Test
    @DisplayName("Should update meeting")
    void updateMeeting_ShouldCallMapper() {
        meetingService.updateMeeting(1, "12:00", "New Desc");
        verify(meetingMapper).updateMeeting(1, "12:00", "New Desc");
    }

    @Test
    @DisplayName("Should create meeting by mentor successfully")
    void createMeetingByMentor_ShouldReturnResponse_WhenAvailable() {
        Time time = Time.valueOf("10:00:00");
        String day = "Monday";
        
        MentorDisplayDetails mentor = new MentorDisplayDetails();
        mentor.setCounselor_id(1);

        when(meetingMapper.checkMeetingAvailability(day, time)).thenReturn(null);
        when(mentorDetailsMapper.getMentorDetaisByEmailId(TEST_EMAIL)).thenReturn(mentor);
        when(common.getRandomThreeDigitNumber()).thenReturn(111);

        Map<String, mentorAvailability> result = meetingService.createMeetingByMentor(day, time);

        assertNotNull(result);
        assertTrue(result.containsKey("Available time updated"));
        verify(meetingMapper).insertAvailableTimings(any(mentorAvailability.class));
    }
}
