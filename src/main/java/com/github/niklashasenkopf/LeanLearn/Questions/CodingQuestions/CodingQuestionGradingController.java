package com.github.niklashasenkopf.LeanLearn.Questions.CodingQuestions;

import com.github.niklashasenkopf.LeanLearn.Questions.CodingQuestions.models.CodingQuestionGradingDTO;
import com.github.niklashasenkopf.LeanLearn.Questions.CodingQuestions.models.CodingQuestionGradingRequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/codingQuestionGrading")
public class CodingQuestionGradingController {
    private final CodingQuestionGradingService codingQuestionGradingService;

    public CodingQuestionGradingController(CodingQuestionGradingService codingQuestionGradingService) {
        this.codingQuestionGradingService = codingQuestionGradingService;
    }

    @PostMapping(
            path = "/grade",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CodingQuestionGradingDTO> generateCodingQuestionGrading(
            @RequestBody CodingQuestionGradingRequestDTO req) throws IOException {
        CodingQuestionGradingDTO result =
                codingQuestionGradingService.generateCodingQuestionResponse(req.getSolution(), req.getDescription(),  req.getInitialCode());
        return ResponseEntity.ok(result);
    }
}
