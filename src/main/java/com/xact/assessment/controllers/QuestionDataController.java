package com.xact.assessment.controllers;
import com.xact.assessment.dtos.AnswerDto;
import com.xact.assessment.models.Answer;
import com.xact.assessment.models.AnswerId;
import com.xact.assessment.services.AnswerService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import org.modelmapper.ModelMapper;
import javax.validation.Valid;
import java.util.List;

@Controller("/v1/notes")
public class QuestionDataController {
    private AnswerService answerService;

    public QuestionDataController(AnswerService answerService) {
        this.answerService = answerService;
    }

    public QuestionDataController() {
    }

    private ModelMapper mapper = new ModelMapper();


    @Post(produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AnswerDto> saveAnswer(@Valid @Body List<AnswerDto> listAnswerDto) {

        for (AnswerDto answerDto : listAnswerDto) {
            AnswerId answerId = mapper.map(answerDto, AnswerId.class);
            Answer answer = mapper.map(answerDto, Answer.class);
            answer.setAnswerId(answerId);
            answerService.saveAnswer(answer);
        }

        return HttpResponse.ok();
    }
}
