package com.github.niklashasenkopf.LeanLearn.Questions.MCQuestion;

import com.github.niklashasenkopf.LeanLearn.Questions.MCQuestion.models.MCQuestion;
import com.github.niklashasenkopf.LeanLearn.Questions.MCQuestion.models.MCQuestionCreateRequestDTO;
import com.github.niklashasenkopf.LeanLearn.Questions.MCQuestion.models.MCQuestionDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MCQuestionMapper {
    MCQuestion toEntity(MCQuestionCreateRequestDTO createRequest);
    MCQuestionDTO toDto(MCQuestion entity);
}
