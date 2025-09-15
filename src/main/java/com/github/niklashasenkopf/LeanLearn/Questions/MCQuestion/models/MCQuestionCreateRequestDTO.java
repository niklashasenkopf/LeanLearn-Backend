package com.github.niklashasenkopf.LeanLearn.Questions.MCQuestion.models;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class MCQuestionCreateRequestDTO {
    @NotNull
    private String question;
    @NotNull
    private List<String> possibleAnswers;
    @NotNull
    private int correctAnswerIndex;
    @NotNull
    private String explanation;
}
