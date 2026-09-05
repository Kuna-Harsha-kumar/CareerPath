package com.example.student.controller;



import java.io.IOException;
import java.util.Map;

import org.apache.tika.exception.TikaException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.student.service.resumeAnalyzer;

@RestController
@RequestMapping("/api/resume")
@CrossOrigin(origins = "http://localhost:5173")
public class resumeController {
    
    private final resumeAnalyzer resumeAnalyzerService;


    public resumeController( resumeAnalyzer resumeAnalyzerService) {
        this.resumeAnalyzerService = resumeAnalyzerService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadResume(@RequestParam("resume") MultipartFile resume) {
        
        try {

            String extractedText = resumeAnalyzerService.extractText(resume);
            Map<String, Object> analysisResult = resumeAnalyzerService.analyzeResume(extractedText);
            return ResponseEntity.ok(analysisResult);
        } catch (IOException | TikaException e) {
            return ResponseEntity.badRequest().body("Error processing resume: " + e.getMessage());
        }
    }
}
