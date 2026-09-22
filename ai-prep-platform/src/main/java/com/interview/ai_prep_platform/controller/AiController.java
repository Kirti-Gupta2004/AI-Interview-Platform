
package com.interview.ai_prep_platform.controller;

import com.interview.ai_prep_platform.dto.ApiResponse;
import com.interview.ai_prep_platform.service.GeminiService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    @Autowired
    private GeminiService geminiService;

    @PostMapping("/upload-resume")
    public ResponseEntity<ApiResponse<String>> uploadResume(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "experience", defaultValue = "Fresher") String experience) {

        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            // 1. PDFBox se PDF Text Extract karein
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String resumeText = pdfStripper.getText(document);

            // 2. Gemini Service Method Call
            String result = geminiService.generateQuestionsFromResume(resumeText, experience);

            // 3. Structured JSON Response
            return ResponseEntity.ok(ApiResponse.success("Questions generated successfully", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to process PDF file: " + e.getMessage()));
        }
    }
}