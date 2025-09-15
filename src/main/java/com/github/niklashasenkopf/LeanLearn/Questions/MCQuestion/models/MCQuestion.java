package com.github.niklashasenkopf.LeanLearn.Questions.MCQuestion.models;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class MCQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2000)
    private String question;

    @ElementCollection
    private List<String> possibleAnswers;

    private int correctAnswerIndex;

    @Column(length = 2000)
    private String explanation;
}
