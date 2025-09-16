package com.github.niklashasenkopf.LeanLearn.Questions.MCQuestion.models;

import java.util.List;

import lombok.Data;

@Data
public class MCQuizDTO {
    private List<MCQuestionDTO> questions;
}
