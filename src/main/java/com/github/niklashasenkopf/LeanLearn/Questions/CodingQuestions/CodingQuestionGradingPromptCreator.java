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
            You are an expert senior software engineer, skilled in reviewing coding exercises
            from junior developers. Your task is to carefully evaluate a student's solution 
            to a coding exercise.

            You will be given:
            1. The exercise description (the assignment).
            2. The initial code provided to the student.
            3. The student's completed solution.

            Your job is to:
            - Assess whether the student's solution correctly addresses the exercise requirements.
            - Point out mistakes, missing parts, or misunderstandings of the assignment.
            - Evaluate the clarity, correctness, and efficiency of the code.
            - Suggest improvements in terms of best practices, readability, and maintainability.
            - Highlight what was done well, so the student knows their strengths.

            The difficulty level of the exercise is: %s

            - EASY: Expect mostly basic syntax and direct application of concepts. Check for simple errors.
            - MEDIUM: Expect correct application of concepts in simple scenarios. Look for logical correctness and structure.
            - HARD: Expect multi-step reasoning or integration of several concepts. Pay attention to algorithmic quality.
            - EXTREME: Expect advanced reasoning and realistic problem-solving. Critique deeply on performance, edge cases, 
              and professional coding practices.
              
            Exercise description: %s
            Initial code: %s
            Student solution: %s
            """, difficulty.name(), exercise.getDescription(), exercise.getInitalCode(), exercise.getStudentSolution());
    }

    public CodingQuestionGradingDTO generateCodingQuestionGrading(CodingExercise exercise)
            throws IOException {

        String schema = """
            {
              "type": "object",
              "properties": {
                "codingQuestionGradingResponse": { "type": "string" }
              },
              "required": ["codingQuestionGradingResponse"],
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

        return chatClient
                .prompt(prompt)
                .call()
                .entity(CodingQuestionGradingDTO.class);
    }
}
