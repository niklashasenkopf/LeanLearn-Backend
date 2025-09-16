package com.github.niklashasenkopf.LeanLearn.Questions.MCQuestion;

import java.io.IOException;
import java.util.List;

import com.github.niklashasenkopf.LeanLearn.Questions.MCQuestion.models.MCQuestionDTO;
import com.github.niklashasenkopf.LeanLearn.Questions.MCQuestion.models.MCQuizDTO;
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
public class MCQuestionPromptCreator {

    private final ChatClient chatClient;

    public MCQuestionPromptCreator(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    private String getSystemInstructions() {
        return """
                You are an expert senior software engineer, skilled in producing detailed, authentic, and correct
                assessments for your junior developers.
                For today's course you assembled some notes which summarize the main points of the course you held today.
                Generate one or several multiple choice question(s) appropriate for a student who wants to learn the courses contents.
                It is important for you to actually test the student's understanding of the material instead of blindly
                reciting facts.
                Thus you want to create a question which requires an actual understanding of the lectures contents.
                The question should ideally consist of a small but real-world realistic scenario
                which challenges the students understanding. You are allowed to use your own knowledge as well when
                creating the questions. Just make sure the provided notes form the base.
                """;
    }

    private String getUserInstructions(int numAnswers, int numQuestions, String fileContent) {
        return String.format("""
        For the following file content you need to create %d question(s) with %d answers of which one is the correct answer.
        Make sure to include an explanation behind the correct answer.
        These are the notes of today's lecture: %s
        """, numQuestions, numAnswers, fileContent);
    }

    /**
     *
     * @param file - The input file supplied by the user. The content gives the base for the created questions
     * @param numAnswers - The number of possible answers desired for the multiple-choice question
     * @return the generated MultipleChoiceQuestion object
     */
    public MCQuestionDTO createMultipleChoiceQuestion(
            MultipartFile file,
            int numAnswers) throws IOException {


        String fileContent = new String(file.getBytes());

        return chatClient
                .prompt()
                .user(getUserInstructions(numAnswers, 1, fileContent))
                .system(getSystemInstructions())
                .call()
                .entity(MCQuestionDTO.class);
    }

    public MCQuizDTO createMCQuiz(
            MultipartFile file,
            int numQuestions) throws IOException {
        String fileContent = new String(file.getBytes());

        String jsonSchema = """
                {
                  "type": "object",
                  "properties": {
                    "questions": {
                      "type": "array",
                      "items": {
                        "type": "object",
                        "properties": {
                          "question": { "type": "string" },
                          "possibleAnswers": {
                            "type": "array",
                            "items": { "type": "string" }
                          },
                          "correctAnswerIndex": { "type": "integer" },
                          "explanation": { "type": "string" }
                        },
                        "required": ["question", "possibleAnswers", "correctAnswerIndex", "explanation"],
                        "additionalProperties": false
                      }
                    }
                  },
                  "required": ["questions"],
                  "additionalProperties": false
                }
                """;

        List<Message> instructions = List.of(
                new SystemMessage(getSystemInstructions()),
                new UserMessage(getUserInstructions(4, numQuestions, fileContent))
        );

        Prompt prompt = new Prompt(
                instructions,
                OpenAiChatOptions.builder()
                        .model(OpenAiApi.ChatModel.GPT_4_O_MINI)
                        .responseFormat(new ResponseFormat(ResponseFormat.Type.JSON_SCHEMA, jsonSchema))
                        .build()
        );

        return chatClient
                .prompt(prompt)
                .call()
                .entity(MCQuizDTO.class);
    }
}
