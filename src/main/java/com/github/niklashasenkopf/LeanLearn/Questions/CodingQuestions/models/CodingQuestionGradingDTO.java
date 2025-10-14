package com.github.niklashasenkopf.LeanLearn.Questions.CodingQuestions.models;

import lombok.Data;

@Data
public class CodingQuestionGradingDTO {
    String codingQuestionGradingResponse;

    //Scoring Rubric
    int requirementsFulfillment; // 0-40 points
    int correctness; // 0-30 points
    int codeQuality; // 0-15 points
    int efficiency; // 0-10 points
    int edgeCases; // 0-5 points
    int fullScore; // 0-100 points
}
