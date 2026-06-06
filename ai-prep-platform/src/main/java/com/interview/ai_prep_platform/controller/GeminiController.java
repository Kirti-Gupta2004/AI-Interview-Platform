package com.interview.ai_prep_platform.controller;

import com.interview.ai_prep_platform.service.GeminiService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
public class GeminiController {

    @Autowired
    private GeminiService geminiService;

    @GetMapping("/generate")
    public ResponseEntity<String> generateQuestions(
            @RequestParam String tech,
            @RequestParam String exp) {
        return ResponseEntity.ok(geminiService.generateInterviewQuestions(tech, exp));
    }

    @PostMapping("/upload-resume")
    public ResponseEntity<String> uploadResume(
            @RequestParam("file") MultipartFile file,
            @RequestParam("exp") String experience) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please upload a valid PDF file.");
        }

        try {
            // 1. PDF Box se text extract karna
            PDDocument document = PDDocument.load(file.getInputStream());
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String extractedText = pdfStripper.getText(document);
            document.close();

            // 2. Calling the method from GeminiService
            String aiResponse = geminiService.generateQuestionsFromResume(extractedText, experience);

            return ResponseEntity.ok(aiResponse);

        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error parsing PDF file: " + e.getMessage());
        }
    }
}