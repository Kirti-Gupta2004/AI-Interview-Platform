package com.interview.ai_prep_platform.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.ai_prep_platform.entity.Question;
import com.interview.ai_prep_platform.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.*;

@Service
public class GeminiService {

    private final QuestionRepository questionRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    // Single Clean Constructor
    public GeminiService(QuestionRepository questionRepository, RestTemplate restTemplate) {
        this.questionRepository = questionRepository;
        this.restTemplate = restTemplate;
        this.objectMapper = new ObjectMapper();
    }

    // 1. Direct Tech aur Experience se question generate aur save karne ka method
    public String generateInterviewQuestions(String technology, String experience) {
        String fullUrl = apiUrl + "?key=" + apiKey;
        String promptText = "Act as an expert technical interviewer. Generate 5 unique technical interview questions on "
                + technology + " for a candidate with " + experience + " experience level. Keep the questions direct.";

        String rawResponseBody = callGeminiApi(fullUrl, promptText);

        // Raw JSON se clean text nikalna
        String cleanResponse = extractCleanText(rawResponseBody);

        // Database mein clean text save karna
        if (cleanResponse != null && !cleanResponse.startsWith("Error")) {
            saveToDatabase(cleanResponse, experience);
        }

        return cleanResponse;
    }

    // 2. Resume Text se question generate aur save karne ka method
    public String generateQuestionsFromResume(String resumeText, String experience) {
        String fullUrl = apiUrl + "?key=" + apiKey;
        String promptText = "Act as an expert technical interviewer. I will provide you the extracted text from a candidate's resume. "
                + "Analyze the technical skills, programming languages, and tools mentioned in the text. "
                + "Generate 10 technical interview questions customized for a candidate with " + experience + " experience level based on those skills.\n\n"
                + "Candidate Resume Text:\n" + resumeText;

        String rawResponseBody = callGeminiApi(fullUrl, promptText);

        // Raw JSON se clean text nikalna
        String cleanResponse = extractCleanText(rawResponseBody);

        // Database mein clean text save karna
        if (cleanResponse != null && !cleanResponse.startsWith("Error")) {
            saveToDatabase(cleanResponse, experience);
        }

        return cleanResponse;
    }

    // Helper method: Gemini API se Raw JSON parse karke clean text nikalne ke liye
    private String extractCleanText(String rawJsonResponse) {
        if (rawJsonResponse == null || rawJsonResponse.startsWith("Error")) {
            return rawJsonResponse;
        }
        try {
            JsonNode rootNode = objectMapper.readTree(rawJsonResponse);
            return rootNode
                    .path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();
        } catch (Exception e) {
            return rawJsonResponse; // Fallback agar parsing fail ho
        }
    }

    // Helper method to call Gemini API
    private String callGeminiApi(String url, String prompt) {
        try {
            Map<String, Object> textMap = new HashMap<>();
            textMap.put("text", prompt);

            Map<String, Object> partsMap = new HashMap<>();
            partsMap.put("parts", Collections.singletonList(textMap));

            Map<String, Object> contentsMap = new HashMap<>();
            contentsMap.put("contents", Collections.singletonList(partsMap));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(contentsMap, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            return response.getBody();
        } catch (Exception e) {
            return "Error calling Gemini API: " + e.getMessage();
        }
    }

    // Helper method to save question in MySQL
    private void saveToDatabase(String questionText, String experience) {
        try {
            Question questionEntity = new Question();
            questionEntity.setExperienceLevel(experience);
            questionEntity.setQuestionText(questionText); // Clean text save hoga
            questionRepository.save(questionEntity);
            System.out.println(">>> SUCCESS: Clean questions successfully saved to MySQL database!");
        } catch (Exception e) {
            System.err.println(">>> ERROR: Failed to save questions to database: " + e.getMessage());
        }
    }
}