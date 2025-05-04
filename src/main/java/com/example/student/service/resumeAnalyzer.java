package com.example.student.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class resumeAnalyzer {

    @Autowired
    profileService profileService;
    
    private final Tika tika = new Tika();

    // Expanded list of skills and their corresponding job roles
    private static final Map<String, List<String>> SKILL_TO_JOBS = Map.ofEntries(
            Map.entry("Java", List.of("Software Engineer", "Backend Developer", "Java Developer")),
            Map.entry("Spring Boot", List.of("Spring Boot Developer", "Backend Engineer")),
            Map.entry("SQL", List.of("Database Administrator", "Data Analyst", "Backend Developer")),
            Map.entry("AWS", List.of("Cloud Engineer", "DevOps Engineer", "AWS Solutions Architect")),
            Map.entry("Python", List.of("Data Scientist", "Machine Learning Engineer", "Software Developer", "Backend Developer")),
            Map.entry("Machine Learning", List.of("AI Engineer", "Data Scientist", "ML Engineer", "AI Researcher")),
            Map.entry("Docker", List.of("DevOps Engineer", "Cloud Engineer", "Software Engineer")),
            Map.entry("React", List.of("Frontend Developer", "Full Stack Developer", "UI/UX Engineer")),
            Map.entry("Node.js", List.of("Backend Developer", "Full Stack Developer")),
            Map.entry("Kubernetes", List.of("DevOps Engineer", "Cloud Engineer", "Site Reliability Engineer")),
            Map.entry("Cybersecurity", List.of("Cybersecurity Analyst", "Penetration Tester", "Security Engineer")),
            Map.entry("Android Development", List.of("Android Developer", "Mobile App Developer")),
            Map.entry("iOS Development", List.of("iOS Developer", "Mobile App Developer")),
            Map.entry("C++", List.of("Software Engineer", "Game Developer", "Embedded Systems Engineer")),
            Map.entry("C#", List.of("Game Developer", "Software Engineer", "Backend Developer")),
            Map.entry("TensorFlow", List.of("Machine Learning Engineer", "AI Researcher", "Data Scientist")),
            Map.entry("Pandas", List.of("Data Analyst", "Data Scientist", "ML Engineer")),
            Map.entry("Power BI", List.of("Business Intelligence Analyst", "Data Analyst")),
            Map.entry("Tableau", List.of("Data Visualization Specialist", "Business Intelligence Analyst")),
            Map.entry("NoSQL", List.of("Database Administrator", "Backend Developer", "Data Engineer")),
            Map.entry("MongoDB", List.of("NoSQL Database Engineer", "Backend Developer", "Data Engineer")),
            Map.entry("Elasticsearch", List.of("Search Engineer", "Data Engineer")),
            Map.entry("Jenkins", List.of("DevOps Engineer", "CI/CD Engineer")),
            Map.entry("Terraform", List.of("Infrastructure Engineer", "Cloud Engineer")),
            Map.entry("GraphQL", List.of("Full Stack Developer", "Backend Developer")),
            Map.entry("TypeScript", List.of("Frontend Developer", "Full Stack Developer"))
    );

    public String extractText(MultipartFile file) throws IOException, TikaException {
        return tika.parseToString(file.getInputStream());
    }

    public Map<String, Object> analyzeResume(String resumeText) {
        String lowerCaseText = resumeText.toLowerCase();

        // Identify matched skills
        List<String> matchedSkills = SKILL_TO_JOBS.keySet().stream()
                .filter(skill -> lowerCaseText.contains(skill.toLowerCase()))
                .collect(Collectors.toList());

        // Identify related job roles
        Set<String> matchedJobs = matchedSkills.stream()
                .flatMap(skill -> SKILL_TO_JOBS.get(skill).stream())
                .collect(Collectors.toSet());

        // Compute skill match score
        double score = ((double) matchedSkills.size() / SKILL_TO_JOBS.size()) * 100;

        // Prepare response
        Map<String, Object> analysisResult = new HashMap<>();
        analysisResult.put("matchedSkills", matchedSkills);
        analysisResult.put("relatedJobs", matchedJobs);
        analysisResult.put("score", score);
        profileService.updateProfile(analysisResult);
        return analysisResult;
    }
    
}
