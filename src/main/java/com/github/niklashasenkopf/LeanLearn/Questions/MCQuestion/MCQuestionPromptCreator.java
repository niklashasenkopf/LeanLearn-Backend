package com.github.niklashasenkopf.LeanLearn.Questions.MCQuestion;

import java.io.IOException;
import com.github.niklashasenkopf.LeanLearn.Questions.MCQuestion.models.MCQuestionDTO;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class MCQuestionPromptCreator {

    private final ChatClient chatClient;

    public MCQuestionPromptCreator(ChatClient chatClient) {
        this.chatClient = chatClient;
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

        String systemInstructions =
                """
                You are an expert senior software engineer, skilled in producing detailed, authentic, and correct
                assessments for your junior developers.
                For today's course you assembled some notes which summarize the main points of the course you held today.
                Generate a multiple choice question appropriate for a student who wants to learn the courses contents.
                It is important for you to actually test the student's understanding of the material instead of blindly
                reciting facts.
                Thus you want to create a question which requires an actual understanding of the lectures contents.
                The question should ideally consist of a small but real-world realistic scenario
                which challenges the students understanding. You are allowed to use your own knowledge as well when
                creating the questions. Just make sure the provided notes form the base.
                """;

        String fileContent = new String(file.getBytes());

        String userInstructions =
                String.format("""
        For the following file content you need to create a question with %d answers of which one is the correct answer.
        Make sure to include an explanation behind the correct answer.
        These are the notes of today's lecture: %s
        """, numAnswers, fileContent);

        return chatClient
                .prompt()
                .user(userInstructions)
                .system(systemInstructions)
                .call()
                .entity(MCQuestionDTO.class);
    }
}
