package com.interview.ai_prep_platform.dto;

import java.util.List;

public class QuestionResponse {
    private String topic;
    private String difficulty;
    private List<String> questions;

    public QuestionResponse() {}

    public QuestionResponse(String topic, String difficulty, List<String> questions) {
        this.topic = topic;
        this.difficulty = difficulty;
        this.questions = questions;
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

    public List<String> getQuestions() {
        return questions;
    }

    public void setQuestions(List<String> questions) {
        this.questions = questions;
    }
}