package com.example.student.service;

import com.example.student.Entity.MeetingDisplayDetails;
import com.example.student.Entity.MentorDisplayDetails;
import com.example.student.Utility.Common;
import com.example.student.dto.meetingDetails;
import com.example.student.dto.mentorDetials;
import com.example.student.mapper.MentorDetailsMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CounselorServiceTest {

    @Mock
    private MentorDetailsMapper mentorDetailsMapper;

    @Mock
    private Common common;

    @Mock
    private JWTService jwtService;

    @InjectMocks
    private counselorService counselorService;

    private final BCryptPasswordEncoder realEncoder = new BCryptPasswordEncoder();

    @Test
    @DisplayName("Should return all counselors")
    void geListoftAllCounselors_ShouldReturnList() {
        List<mentorDetials> list = new ArrayList<>();
        when(mentorDetailsMapper.getAllCounselors()).thenReturn(list);

        List<mentorDetials> result = counselorService.geListoftAllCounselors();
        assertEquals(list, result);
        verify(mentorDetailsMapper).getAllCounselors();
    }

    @Test
    @DisplayName("Should return students of mentor")
    void getStudentsofMentor_ShouldReturnList() {
        List<meetingDetails> list = new ArrayList<>();
        when(mentorDetailsMapper.getCouncelorStudents(1)).thenReturn(list);

        List<meetingDetails> result = counselorService.getStudentsofMentor(1);
        assertEquals(list, result);
        verify(mentorDetailsMapper).getCouncelorStudents(1);
    }

    @Test
    @DisplayName("Should return available timing details")
    void getAvailableTimingDetails_ShouldReturnList() {
        List<MeetingDisplayDetails> list = new ArrayList<>();
        when(mentorDetailsMapper.getAvailableTimings(1)).thenReturn(list);

        List<MeetingDisplayDetails> result = counselorService.getAvailableTimingDetails(1);
        assertEquals(list, result);
        verify(mentorDetailsMapper).getAvailableTimings(1);
    }

    @Test
    @DisplayName("Should insert mentor details successfully")
    void insertMentorDetails_ShouldReturnSuccess_WhenNewEmail() {
        mentorDetials dto = new mentorDetials();
        dto.setEmailId("new@example.com");
        dto.setPassword("pass");

        when(mentorDetailsMapper.getMentorDetaisByEmailId("new@example.com")).thenReturn(null);
        when(common.getRandomThreeDigitNumber()).thenReturn(123);

        String result = counselorService.insertMentorDetails(dto);

        assertEquals("User addedd successfully", result);
        verify(mentorDetailsMapper).insertUserDetails(any(mentorDetials.class));
    }

    @Test
    @DisplayName("Should return error when email exists")
    void insertMentorDetails_ShouldReturnError_WhenEmailExists() {
        mentorDetials dto = new mentorDetials();
        dto.setEmailId("existing@example.com");

        when(mentorDetailsMapper.getMentorDetaisByEmailId("existing@example.com")).thenReturn(new MentorDisplayDetails());

        String result = counselorService.insertMentorDetails(dto);

        assertEquals("Emailid already existing", result);
        verify(mentorDetailsMapper, never()).insertUserDetails(any());
    }

    @Test
    @DisplayName("Should authenticate mentor successfully")
    void authenticateMentorByEmail_ShouldReturnToken_WhenValid() {
        String email = "mentor@example.com";
        String pass = "password";
        MentorDisplayDetails user = new MentorDisplayDetails();
        user.setCounselor_id(1);
        user.setPassword(realEncoder.encode(pass));

        when(jwtService.generateToken(email)).thenReturn("valid-token");
        when(mentorDetailsMapper.getMentorDetaisByEmailId(email)).thenReturn(user);

        Map<String, MentorDisplayDetails> response = counselorService.authenticateMentorByEmail(email, pass);

        assertTrue(response.containsKey("valid-token"));
        assertEquals(user, response.get("valid-token"));
    }

    @Test
    @DisplayName("Should return User not found when ID is 0")
    void authenticateMentorByEmail_ShouldReturnNotFound_WhenId0() {
        String email = "mentor@example.com";
        MentorDisplayDetails user = new MentorDisplayDetails();
        user.setCounselor_id(0);

        when(mentorDetailsMapper.getMentorDetaisByEmailId(email)).thenReturn(user);

        Map<String, MentorDisplayDetails> response = counselorService.authenticateMentorByEmail(email, "pass");

        assertTrue(response.containsKey("User not found"));
    }

    @Test
    @DisplayName("Should return Password Incorrect when password mismatches")
    void authenticateMentorByEmail_ShouldReturnIncorrect_WhenPassMismatch() {
        String email = "mentor@example.com";
        MentorDisplayDetails user = new MentorDisplayDetails();
        user.setCounselor_id(1);
        user.setPassword(realEncoder.encode("correct"));

        when(mentorDetailsMapper.getMentorDetaisByEmailId(email)).thenReturn(user);

        Map<String, MentorDisplayDetails> response = counselorService.authenticateMentorByEmail(email, "wrong");

        assertTrue(response.containsKey("Password Incorrect"));
    }
}
