package com.github.niklashasenkopf.LeanLearn.Questions.CodingQuestions;

import java.io.IOException;

import com.github.niklashasenkopf.LeanLearn.Questions.CodingQuestions.models.CodingQuestionDTO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/codingQuestion")
public class CodingQuestionController {

    private final CodingQuestionService codingQuestionService;

    public CodingQuestionController(CodingQuestionService codingQuestionService) {
        this.codingQuestionService = codingQuestionService;
    }

    @PostMapping(
            path = "/createNew",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CodingQuestionDTO> generateCodingExerciseForFile(
            @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(codingQuestionService.generateCodingQuestion(file));
    }
}
