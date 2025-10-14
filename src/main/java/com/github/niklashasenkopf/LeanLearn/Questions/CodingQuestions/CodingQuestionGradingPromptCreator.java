package com.github.niklashasenkopf.LeanLearn.Questions.CodingQuestions;

import com.github.niklashasenkopf.LeanLearn.Questions.CodingQuestions.models.CodingQuestionGradingDTO;
import com.github.niklashasenkopf.LeanLearn.Questions.CodingQuestions.models.CodingExercise;
import com.github.niklashasenkopf.LeanLearn.Questions.MCQuestion.models.Difficulty;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.openai.api.ResponseFormat;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class CodingQuestionGradingPromptCreator {

    private final ChatClient chatClient;

    public CodingQuestionGradingPromptCreator(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    private String getSystemInstructions(Difficulty difficulty, CodingExercise exercise) {
        return String.format("""
        You are an expert senior software engineer who reviews and grades coding exercises from junior developers.

        You will be given:
        1) The exercise description (the assignment).
        2) The initial code provided to the student.
        3) The student's completed solution.

        Your job:
        - Assess whether the student's solution addresses the requirements.
        - Point out mistakes, missing parts, or misunderstandings.
        - Evaluate clarity, correctness, efficiency, edge cases, and maintainability.
        - Suggest concrete, actionable improvements.
        - Highlight what was done well.

        Difficulty level: %s

        Difficulty guidance:
        - EASY: Basic syntax and concept application; check for simple errors.
        - MEDIUM: Correct application in simple scenarios; check logic and structure.
        - HARD: Multi-step reasoning and multiple concepts; consider algorithmic quality.
        - EXTREME: Advanced reasoning and realistic problem-solving; critique performance, edge cases, and professional practices.

        SCORING RUBRIC (total must sum to 0–100):
        - requirements_fulfillment: 0–40 (meets stated requirements/spec)
        - correctness:             0–30 (logical/functional correctness; passes plausible tests)
        - code_quality:            0–15 (readability, naming, structure, best practices)
        - efficiency:              0–10 (time/space complexity for the task difficulty)
        - edge_cases:              0–5  (handles null/empty/boundary/error conditions)

        Rules:
        - Compute each sub-score as an integer. Sum them to produce "grade" (0–100). Clamp to 0–100 if necessary.
        - 100%% represents a flawless, professional-grade solution for the given difficulty.
        - Be specific and concise. Provide concrete examples (e.g., variable names, functions) in feedback.
        - DO NOT include any text outside the JSON. No markdown, no explanations before/after.
        - If information is missing, state the assumption inside the JSON "summary".

        Exercise description:
        %s

        Initial code:
        %s

        Student solution:
        %s
        """,
                difficulty.name(),
                exercise.getDescription(),
                exercise.getInitalCode(),
                exercise.getStudentSolution()
        );
    }


    public CodingQuestionGradingDTO generateCodingQuestionGrading(CodingExercise exercise)
            throws IOException {

        String schema = """
            {
              "type": "object",
              "properties": {
                "codingQuestionGradingResponse": { "type": "string", "minLength": 1 },
                "requirementsFulfillment": {"type": "integer", "minimum": 0, "maximum": 40},
                "correctness": {"type": "integer", "minimum": 0, "maximum": 30},
                "codeQuality": {"type": "integer", "minimum": 0, "maximum": 15},
                "efficiency": {"type": "integer", "minimum": 0, "maximum": 10},
                "edgeCases": {"type": "integer", "minimum": 0, "maximum": 5}
              },
              "required": ["codingQuestionGradingResponse",
               "requirementsFulfillment",
               "correctness",
               "codeQuality",
               "efficiency",
               "edgeCases"],
              "additionalProperties": false
            }
            """;


        List<Message> instructions = List.of(
                new SystemMessage(getSystemInstructions(Difficulty.MEDIUM, exercise))
        );
        Prompt prompt = new Prompt(
                instructions,
                OpenAiChatOptions.builder()
                        .model(OpenAiApi.ChatModel.GPT_4_O_MINI)
                        .responseFormat(new ResponseFormat(ResponseFormat.Type.JSON_SCHEMA, schema))
                        .build()
        );

        CodingQuestionGradingDTO answer = chatClient
                .prompt(prompt)
                .call()
                .entity(CodingQuestionGradingDTO.class);

        if(answer == null){
            throw new IllegalStateException("LLM returned no parsable answer");
        }

        int fullScore = answer.getRequirementsFulfillment() +
                answer.getCorrectness() +
                answer.getCodeQuality() +
                answer.getEfficiency() +
                answer.getEdgeCases();

        answer.setFullScore(fullScore);

        return answer;
    }
}
