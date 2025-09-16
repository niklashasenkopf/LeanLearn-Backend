package com.github.niklashasenkopf.LeanLearn.Questions.MCQuestion;

import java.io.IOException;
import java.util.List;

import com.github.niklashasenkopf.LeanLearn.Questions.MCQuestion.models.MCQuestion;
import com.github.niklashasenkopf.LeanLearn.Questions.MCQuestion.models.MCQuestionCreateRequestDTO;
import com.github.niklashasenkopf.LeanLearn.Questions.MCQuestion.models.MCQuestionDTO;
import com.github.niklashasenkopf.LeanLearn.Questions.MCQuestion.models.MCQuizDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/mcQuestion")
public class MCQuestionController {

    private final MCQuestionService mcQuestionService;
    private final MCQuestionMapper mapper;

    public MCQuestionController(
            MCQuestionService mcQuestionService,
            MCQuestionMapper mapper
    ) {
        this.mcQuestionService = mcQuestionService;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<MCQuestion>> getAllQuestions() {
        return ResponseEntity.ok(mcQuestionService.getAllQuestions());
    }

    @PostMapping
    public ResponseEntity<MCQuestionDTO> saveQuestion(
            @RequestBody MCQuestionCreateRequestDTO mcQuestionCreateRequest) {

        MCQuestion savedQuestion = mcQuestionService.saveQuestion(mcQuestionCreateRequest);

        MCQuestionDTO toReturn = mapper.toDto(savedQuestion);

        return ResponseEntity.status(HttpStatus.CREATED).body(toReturn);
    }

    @PostMapping(path = "/createNew",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<MCQuestionDTO> generateQuestionForFile(
            @RequestParam("file") MultipartFile fileToUpload) throws IOException {

        return ResponseEntity.ok(mcQuestionService.generateQuestion(fileToUpload));
    }

    @PostMapping(path = "/createQuiz",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<MCQuizDTO> generateMcQuizForFile(
            @RequestParam("file") MultipartFile file) throws IOException {

        return ResponseEntity.ok(mcQuestionService.generateMcQuiz(file));
    }
}
