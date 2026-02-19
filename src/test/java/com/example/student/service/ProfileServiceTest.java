package com.example.student.service;

import com.example.student.Entity.StudentDisplayDetails;
import com.example.student.dto.ProfileUpdateDetails;
import com.example.student.dto.mentorDetials;
import com.example.student.mapper.profilemapper;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ProfileServiceTest {

    @Mock
    private profilemapper profilemapper;

    @Mock
    private userMapper userMapper;

    @InjectMocks
    private profileService profileService;

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
    @DisplayName("Should update profile based on analysis result")
    void updateProfile_ShouldCallMapper() {
        Map<String, Object> analysisResult = new HashMap<>();
        analysisResult.put("key1", "val1");
        analysisResult.put("key2", "jobs");
        analysisResult.put("key3", "skills");

        profileService.updateProfile(analysisResult);

        verify(profilemapper).updateSkills(eq("skills"), eq("jobs"), eq(TEST_EMAIL));
    }

    @Test
    @DisplayName("Should return student details")
    void getStudentDetails_ShouldReturnList() {
        List<StudentDisplayDetails> list = new ArrayList<>();
        when(profilemapper.getStudentDetails(TEST_EMAIL)).thenReturn(list);
        assertEquals(list, profileService.getStudentDetails(TEST_EMAIL));
    }

    @Test
    @DisplayName("Should return counselor details")
    void getCounselorDetails_ShouldReturnDto() {
        mentorDetials dto = new mentorDetials();
        when(profilemapper.getCounselorDetails(1)).thenReturn(dto);
        assertEquals(dto, profileService.getCounselorDetails(1));
    }

    @Test
    @DisplayName("Should update password")
    void updatePassword_ShouldCallMapper() {
        profileService.updatePassword("newPass", TEST_EMAIL);
        verify(profilemapper).updatePassword(anyString(), eq(TEST_EMAIL));
    }

    @Test
    @DisplayName("Should update profile details successfully")
    void updateProfileDetails_ShouldReturnSuccess() {
        ProfileUpdateDetails detailsDto = new ProfileUpdateDetails();
        StudentDisplayDetails existingUser = new StudentDisplayDetails();
        existingUser.setStudent_id(123);

        when(userMapper.userDetails(TEST_EMAIL)).thenReturn(existingUser);

        String result = profileService.updateProfileDetails(detailsDto);

        assertEquals("User Details updated successfully", result);
        assertEquals(123, detailsDto.getStudentId());
        verify(profilemapper).updateUserDetails(detailsDto);
    }
}
