package com.interview.ai_prep_platform.service;

import com.interview.ai_prep_platform.entity.Question;
import com.interview.ai_prep_platform.repository.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GeminiServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private GeminiService geminiService;

    @BeforeEach
    void setUp() {
        // Mocking @Value fields manually for isolated testing
        ReflectionTestUtils.setField(geminiService, "apiKey", "test-api-key");
        ReflectionTestUtils.setField(geminiService, "apiUrl", "http://test-url.com");
    }

    @Test
    void testGenerateInterviewQuestions_Success() {
        // 1. Dummy JSON response from Gemini API mock
        String mockJsonResponse = "{\n" +
                "  \"candidates\": [{\n" +
                "    \"content\": {\n" +
                "      \"parts\": [{\"text\": \"1. What is Java?\"}]\n" +
                "    }\n" +
                "  }]\n" +
                "}";

        ResponseEntity<String> responseEntity = new ResponseEntity<>(mockJsonResponse, HttpStatus.OK);

        // 2. Mocking RestTemplate call
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class)))
                .thenReturn(responseEntity);

        // 3. Service method invocation
        String result = geminiService.generateInterviewQuestions("Java", "2 years");

        // 4. Assertions & Verification
        assertNotNull(result);
        assertEquals("1. What is Java?", result);
        verify(questionRepository, times(1)).save(any(Question.class));
    }
}