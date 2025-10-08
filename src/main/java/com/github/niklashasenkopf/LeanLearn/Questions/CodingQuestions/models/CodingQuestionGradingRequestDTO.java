package com.github.niklashasenkopf.LeanLearn.Questions.CodingQuestions.models;

import lombok.Data;

@Data
public class CodingQuestionGradingRequestDTO {
        private String description;
        private String initialCode;
        private String solution;
}
