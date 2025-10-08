package com.github.niklashasenkopf.LeanLearn.Questions.CodingQuestions;

import com.github.niklashasenkopf.LeanLearn.Questions.CodingQuestions.models.CodingExercise;
import com.github.niklashasenkopf.LeanLearn.Questions.CodingQuestions.models.CodingQuestionGradingDTO;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CodingQuestionGradingService {
    private final CodingQuestionGradingPromptCreator promptCreator;

    CodingQuestionGradingService(CodingQuestionGradingPromptCreator promptCreator) {
        this.promptCreator = promptCreator;
    }

    public CodingQuestionGradingDTO generateCodingQuestionResponse(String solution, String description, String initialCode)
            throws IOException {
        CodingExercise codingExercise = new CodingExercise();
        codingExercise.setStudentSolution(solution);
        codingExercise.setDescription(description);
        codingExercise.setInitalCode(initialCode);
        return promptCreator.generateCodingQuestionGrading(codingExercise);
    }
}
