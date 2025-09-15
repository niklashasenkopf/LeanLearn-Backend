package com.github.niklashasenkopf.LeanLearn.Questions.MCQuestion;

import com.github.niklashasenkopf.LeanLearn.Questions.MCQuestion.models.MCQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MCQuestionRepository extends JpaRepository<MCQuestion, Long>{}
