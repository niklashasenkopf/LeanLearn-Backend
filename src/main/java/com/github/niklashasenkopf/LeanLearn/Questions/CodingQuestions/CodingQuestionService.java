package com.github.niklashasenkopf.LeanLearn.Questions.CodingQuestions;

import java.io.IOException;

import com.github.niklashasenkopf.LeanLearn.Questions.CodingQuestions.models.CodingQuestionDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CodingQuestionService {

    private final CodingQuestionPromptCreator promptCreator;

    CodingQuestionService(CodingQuestionPromptCreator promptCreator) {
        this.promptCreator = promptCreator;
    }

    public CodingQuestionDTO generateCodingQuestion(MultipartFile file)
            throws IOException {
        return promptCreator.generateCodingQuestion(file);
    }
}
