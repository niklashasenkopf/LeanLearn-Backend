package com.github.niklashasenkopf.LeanLearn.Questions.MCQuestion.models;

import java.util.List;

import lombok.Data;

@Data
public class MCQuestionDTO {
    private String question;
    private List<String> possibleAnswers;
    private int correctAnswerIndex;
    private String explanation;
}
