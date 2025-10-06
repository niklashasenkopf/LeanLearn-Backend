package com.github.niklashasenkopf.LeanLearn.Questions.CodingQuestions.models;

import com.github.niklashasenkopf.LeanLearn.Questions.MCQuestion.models.Difficulty;
import lombok.Data;

@Data
public class Exercise {
    String description;
    String initalCode;
    String StudentSolution;
}
