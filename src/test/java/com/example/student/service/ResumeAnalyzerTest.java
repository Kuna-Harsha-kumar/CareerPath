package com.example.student.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResumeAnalyzerTest {

    @Mock
    private profileService profileService;

    @InjectMocks
    private resumeAnalyzer resumeAnalyzer;

    @Test
    @DisplayName("Should extract text from mock multipart file")
    void extractText_ShouldReturnText() throws Exception {
        MockMultipartFile file = new MockMultipartFile("resume", "resume.txt", "text/plain", "Test resume content".getBytes());
        String result = resumeAnalyzer.extractText(file);
        // Tika might add some extra characters/metadata, but it should contain the text
        assertTrue(result.contains("Test resume content"));
    }

    @Test
    @DisplayName("Should analyze resume and find matching skills and jobs")
    void analyzeResume_ShouldReturnAnalysisResult() {
        String resumeText = "I am a Java developer experienced with Spring Boot and SQL.";

        Map<String, Object> result = resumeAnalyzer.analyzeResume(resumeText);

        assertNotNull(result);
        List<String> matchedSkills = (List<String>) result.get("matchedSkills");
        Set<String> relatedJobs = (Set<String>) result.get("relatedJobs");
        Double score = (Double) result.get("score");

        assertTrue(matchedSkills.contains("Java"));
        assertTrue(matchedSkills.contains("Spring Boot"));
        assertTrue(matchedSkills.contains("SQL"));
        
        // Check if some related jobs are present
        assertTrue(relatedJobs.contains("Software Engineer") || relatedJobs.contains("Backend Developer") || relatedJobs.contains("Java Developer"));
        
        assertTrue(score > 0);
        verify(profileService).updateProfile(anyMap());
    }
}
