package com.github.niklashasenkopf.LeanLearn.Questions.CodingQuestions;

import com.github.niklashasenkopf.LeanLearn.Questions.CodingQuestions.models.CodingQuestionGradingDTO;
import com.github.niklashasenkopf.LeanLearn.Questions.CodingQuestions.models.Exercise;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class CodingQuestionGradingService {
    private final CodingQuestionGradingPromptCreator promptCreator;

    CodingQuestionGradingService(CodingQuestionGradingPromptCreator promptCreator) {
        this.promptCreator = promptCreator;
    }

    public CodingQuestionGradingDTO generateCodingQuestionResponse(String solution, String description, String initialCode)
            throws IOException {
        Exercise exercise = new Exercise();
        exercise.setStudentSolution(solution);
        exercise.setDescription(description);
        exercise.setInitalCode(initialCode);
        return promptCreator.generateCodingQuestionGrading(exercise);
    }
}
