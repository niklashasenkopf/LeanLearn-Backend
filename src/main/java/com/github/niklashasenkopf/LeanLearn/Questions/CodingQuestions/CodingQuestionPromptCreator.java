package com.github.niklashasenkopf.LeanLearn.Questions.CodingQuestions;

import java.io.IOException;
import java.util.List;

import com.github.niklashasenkopf.LeanLearn.Questions.CodingQuestions.models.CodingQuestionDTO;
import com.github.niklashasenkopf.LeanLearn.Questions.MCQuestion.models.Difficulty;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.openai.api.ResponseFormat;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class CodingQuestionPromptCreator {

    private final ChatClient chatClient;

    public CodingQuestionPromptCreator(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    private String getSystemInstructions(Difficulty difficulty) {
        return String.format("""
                You are an expert senior software engineer, skilled in producing detailed, authentic, and correct
                assessments for your junior developers.
                For today's course you assembled some notes which summarize the main points of the course you held today.
                Generate a coding codingExercise appropriate for a student who wants to learn the courses contents.
                It is important for you to actually test the student's understanding of the material instead of blindly
                reciting facts.
                Thus you want to create an codingExercise which requires an actual understanding of the lectures contents.
                The question should ideally consist of a small but real-world realistic scenario
                which challenges the students understanding. If applicable you can supply existing code which the student
                will extend on in the codingExercise.
                You are allowed to use your own knowledge as well when
                creating the questions. Just make sure the provided notes form the base.
                
                The difficulty level of the generated questions must be: %s
                
                - EASY: Focus on basic recall and direct application of key facts or definitions.
                - MEDIUM: Require understanding of concepts and the ability to apply them in simple scenarios.
                - HARD: Involve multi-step reasoning, comparisons, or integration of multiple concepts.
                - EXTREME: Use complex, real-world scenarios that require deep problem solving, critical thinking,
                  and careful distinction between very similar answers.
                """,difficulty.name());
    }

    private String getUserInstructions(String fileContent) {
        return String.format("""
        For the following file content you need to create a coding codingExercise.
        These are the notes of today's lecture: %s
        """, fileContent);
    }

    public CodingQuestionDTO generateCodingQuestion(
            MultipartFile file
    ) throws IOException {

        String fileContent = new String(file.getBytes());

        String schema = """
            {
              "type": "object",
              "properties": {
                "exerciseDescription": { "type": "string" },
                "initialCode": { "type": "string" }
              },
              "required": ["exerciseDescription", "initialCode"],
              "additionalProperties": false
            }
            """;

        List<Message> instructions = List.of(
                new SystemMessage(getSystemInstructions(Difficulty.MEDIUM)),
                new UserMessage(getUserInstructions(fileContent))
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
                .entity(CodingQuestionDTO.class);
    }
}
