package com.interview.ai_prep_platform.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public String generateInterviewQuestions(String tech, int exp) {
        String prompt = String.format(
                "Generate 5 technical interview questions for a %s developer with %d years of experience. Provide only the questions without additional fluff.",
                tech, exp
        );
        return callGeminiApi(prompt);
    }

    public String generateQuestionsFromResume(String resumeText, String experience) {
        String prompt = String.format(
                "You are a technical interviewer. Based on the following resume content, generate 5 detailed interview questions focusing on their projects and key technical skills for a candidate with %s years of experience.\n\nResume:\n%s",
                experience, resumeText
        );
        return callGeminiApi(prompt);
    }

    private String callGeminiApi(String promptText) {
        String fullUrl = apiUrl + "?key=" + apiKey;

        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(Map.of("text", promptText)))
                )
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(fullUrl, entity, String.class);

            // Raw response me se exact text extract karna
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            return root.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();

        } catch (Exception e) {
            return "Error parsing AI response: " + e.getMessage();
        }
    }
}