package com.example.surveyserver.service;

import com.example.surveyserver.exception.ResourceNotFoundException;
import com.example.surveyserver.model.Option;
import com.example.surveyserver.model.Question;
import com.example.surveyserver.model.Survey;
import com.example.surveyserver.model.SurveyReply;
import com.example.surveyserver.repository.SurveyReplyRepository;
import com.example.surveyserver.repository.SurveyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SurveyService {

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private SurveyReplyRepository surveyReplyRepository;

    public Survey createSurvey(Survey survey) {
        List<Question> questions = survey.getQuestions();
        questions.forEach(question -> {
            // bidirectional association to reduce sql statements
            // https://vladmihalcea.com/the-best-way-to-map-a-onetomany-association-with-jpa-and-hibernate/
            question.setSurvey(survey);
            List<Option> options = question.getOptions();
            if (options != null) {
                options.forEach(option -> {
                    option.setQuestion(question);
                });
            }
        });
        return surveyRepository.save(survey);
    }

    public Survey getSurvey(Integer id) {
        return surveyRepository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new ResourceNotFoundException("Survey not found with ID: " + id));
    }

    public Page<Survey> getAllSurveys(Pageable pageable) {
        /* ChatGPT
        In your example, FetchType.LAZY should work as expected for the @OneToMany relationship between Survey and Question.
        However, when you are serializing the Survey object into JSON format and the getter for the questions field is called,
        it will trigger the lazy loading of the associated Question objects.
        The most common cause of this behavior is using the default Jackson serializer that calls the getter methods on the object,
        * thus causing the associated collection to be fetched.

        To avoid the automatic fetching of the questions field when serializing the Survey object, you can use the @JsonIgnore
        annotation on the questions field in the Survey entity:

        With the @JsonIgnore annotation, the questions field will not be serialized, and the associated Question objects will not be fetched unless explicitly called in the code.

        Please note that with this change, the questions field will not be included in the JSON output when you call the getAllSurveys() method.
        If you need to load the questions field explicitly, you will have to do so in your service or repository layer.
        */
        return surveyRepository.findByIsTemplateFalseAndIsDeletedFalseOrderByIdDesc(pageable);
    }

    public Survey updateSurvey(Survey updatedSurvey) {
        Survey existingSurvey = surveyRepository.findByIdAndIsDeletedFalse(updatedSurvey.getId())
                .orElseThrow(() -> new RuntimeException("Survey not found"));

        // Update the survey details
        existingSurvey.setTitle(updatedSurvey.getTitle());
        existingSurvey.setDescription(updatedSurvey.getDescription());
        existingSurvey.setIsTemplate(updatedSurvey.getIsTemplate());
        existingSurvey.setIsAnonymous(updatedSurvey.getIsAnonymous());
        existingSurvey.setAllowResubmit(updatedSurvey.getAllowResubmit());
        existingSurvey.setStartTime(updatedSurvey.getStartTime());
        existingSurvey.setEndTime(updatedSurvey.getEndTime());
        existingSurvey.setMaxReplies(updatedSurvey.getMaxReplies());

        // Find questions and options to remove
        Set<Integer> updatedQuestionIds = updatedSurvey.getQuestions().stream()
                .map(Question::getId)
                .collect(Collectors.toSet());

        List<Question> questionsToRemove = existingSurvey.getQuestions().stream()
                .filter(question -> !updatedQuestionIds.contains(question.getId()))
                .collect(Collectors.toList());

        existingSurvey.getQuestions().removeAll(questionsToRemove);
        questionsToRemove.forEach(question -> question.setSurvey(null));

        // Update the questions and options
        for (Question question : updatedSurvey.getQuestions()) {
            Question existingQuestion = existingSurvey.getQuestions().stream()
                    .filter(q -> q.getId().equals(question.getId()))
                    .findFirst()
                    .orElse(null);

            if (existingQuestion != null) {
                existingQuestion.setQuestionType(question.getQuestionType());
                existingQuestion.setQuestionText(question.getQuestionText());
                existingQuestion.setIsMandatory(question.getIsMandatory());
                existingQuestion.setMinSelection(question.getMinSelection());
                existingQuestion.setMaxSelection(question.getMaxSelection());

                Set<Integer> updatedOptionIds = question.getOptions().stream()
                        .map(Option::getId)
                        .collect(Collectors.toSet());

                List<Option> optionsToRemove = existingQuestion.getOptions().stream()
                        .filter(option -> !updatedOptionIds.contains(option.getId()))
                        .collect(Collectors.toList());

                existingQuestion.getOptions().removeAll(optionsToRemove);
                optionsToRemove.forEach(option -> option.setQuestion(null));

                for (Option option : question.getOptions()) {
                    Option existingOption = existingQuestion.getOptions().stream()
                            .filter(o -> o.getId().equals(option.getId()))
                            .findFirst()
                            .orElse(null);

                    if (existingOption != null) {
                        existingOption.setOptionText(option.getOptionText());
                    } else {
                        existingQuestion.getOptions().add(option);
                        option.setQuestion(existingQuestion);
                    }
                }
            } else {
                existingSurvey.getQuestions().add(question);
                question.setSurvey(existingSurvey);
                for (Option option : question.getOptions()) {
                    option.setQuestion(question);
                }
            }
        }

        // Save the updated survey
        surveyRepository.save(existingSurvey);

        return existingSurvey;
    }

    public Page<Survey> getSurveysByUser(Integer userId, Pageable pageable) {
        return surveyRepository.findByUserIdAndIsTemplateFalseAndIsDeletedFalseOrderByIdDesc(userId, pageable);
    }

    public Page<Survey> getRepliedSurveysByUser(Integer userId, Pageable pageable) {
        Page<SurveyReply> surveyReplies = surveyReplyRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        return surveyReplies.map(SurveyReply::getSurvey);
    }

    public Page<Survey> getAllTemplates(Pageable pageable) {
        return surveyRepository.findByIsTemplateTrueAndIsDeletedFalseOrderByIdDesc(pageable);
    }

    public Survey deleteSurvey(Integer id) {
        Survey survey = getSurvey(id);
        if (survey != null) {
            survey.setIsDeleted(true);
            return updateSurvey(survey);
        }
        return null;
    }

}
