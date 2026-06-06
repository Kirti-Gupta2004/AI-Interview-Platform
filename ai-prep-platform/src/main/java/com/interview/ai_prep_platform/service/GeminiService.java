package com.interview.ai_prep_platform.service;

import com.interview.ai_prep_platform.entity.Question;
import com.interview.ai_prep_platform.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.*;

@Service
public class GeminiService {

    @Autowired
    private QuestionRepository questionRepository;

    private final String apiKey = "YOUR_GEMINI_API_KEY";
    private final String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent";
    private final RestTemplate restTemplate = new RestTemplate();

    // 1. Direct Tech aur Experience se question generate aur save karne ka method
    public String generateInterviewQuestions(String technology, String experience) {
        String fullUrl = apiUrl + "?key=" + apiKey;
        String promptText = "Act as an expert technical interviewer. Generate 5 unique technical interview questions on "
                + technology + " for a candidate with " + experience + " experience level. Keep the questions direct.";

        String responseBody = callGeminiApi(fullUrl, promptText);

        // Database mein save karna
        if (responseBody != null && !responseBody.startsWith("Error")) {
            saveToDatabase(responseBody, experience);
        }

        return responseBody;
    }

    // 2. Resume Text se question generate aur save karne ka method
    public String generateQuestionsFromResume(String resumeText, String experience) {
        String fullUrl = apiUrl + "?key=" + apiKey;
        String promptText = "Act as an expert technical interviewer. I will provide you the extracted text from a candidate's resume. "
                + "Analyze the technical skills, programming languages, and tools mentioned in the text. "
                + "Generate 10 technical interview questions customized for a candidate with " + experience + " experience level based on those skills.\n\n"
                + "Candidate Resume Text:\n" + resumeText;

        String responseBody = callGeminiApi(fullUrl, promptText);

        // Database mein save karna
        if (responseBody != null && !responseBody.startsWith("Error")) {
            saveToDatabase(responseBody, experience);
        }

        return responseBody;
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
    private void saveToDatabase(String rawJson, String experience) {
        try {
            Question questionEntity = new Question();
            questionEntity.setExperienceLevel(experience);
            questionEntity.setQuestionText(rawJson); // Poora JSON response store ho jayega
            questionRepository.save(questionEntity);
            System.out.println(">>> SUCCESS: Questions successfully saved to MySQL database!");
        } catch (Exception e) {
            System.err.println(">>> ERROR: Failed to save questions to database: " + e.getMessage());
        }
    }
}