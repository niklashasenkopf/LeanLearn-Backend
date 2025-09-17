package com.github.niklashasenkopf.LeanLearn.Questions.MCQuestion;
import java.io.IOException;
import java.util.List;

import com.github.niklashasenkopf.LeanLearn.Questions.MCQuestion.models.Difficulty;
import com.github.niklashasenkopf.LeanLearn.Questions.MCQuestion.models.MCQuestion;
import com.github.niklashasenkopf.LeanLearn.Questions.MCQuestion.models.MCQuestionCreateRequestDTO;
import com.github.niklashasenkopf.LeanLearn.Questions.MCQuestion.models.MCQuestionDTO;
import com.github.niklashasenkopf.LeanLearn.Questions.MCQuestion.models.MCQuizDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MCQuestionService {

    private final MCQuestionPromptCreator mcQuestionPromptCreator;
    private final MCQuestionRepository mcQuestionRepository;
    private final MCQuestionMapper mcQuestionMapper;

    public MCQuestionService(
            MCQuestionPromptCreator mcQuestionPromptCreator,
            MCQuestionRepository mcQuestionRepository,
            MCQuestionMapper mcQuestionMapper
    ) {
        this.mcQuestionPromptCreator = mcQuestionPromptCreator;
        this.mcQuestionRepository = mcQuestionRepository;
        this.mcQuestionMapper = mcQuestionMapper;
    }

    public List<MCQuestion> getAllQuestions() {
        return mcQuestionRepository.findAll();
    }

    public MCQuestionDTO generateQuestion(MultipartFile file) throws IOException {
        return mcQuestionPromptCreator.createMultipleChoiceQuestion(file, 4);
    }

    public MCQuestion saveQuestion(MCQuestionCreateRequestDTO mcQuestionCreateRequest) {
        MCQuestion entity = mcQuestionMapper.toEntity(mcQuestionCreateRequest);
        return mcQuestionRepository.save(entity);
    }

    public MCQuizDTO generateMcQuiz(
            MultipartFile file,
            Difficulty difficulty
    )
            throws IOException {

        return mcQuestionPromptCreator.createMCQuiz(file, 5, difficulty);

    }
}
