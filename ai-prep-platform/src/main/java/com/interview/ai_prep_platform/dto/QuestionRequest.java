package com.interview.ai_prep_platform.dto;

public class QuestionRequest {
    private String topic;
    private String difficulty; // e.g., EASY, MEDIUM, HARD

    public QuestionRequest() {}

    public QuestionRequest(String topic, String difficulty) {
        this.topic = topic;
        this.difficulty = difficulty;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}