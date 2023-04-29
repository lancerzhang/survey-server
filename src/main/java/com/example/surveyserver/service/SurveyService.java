package com.example.surveyserver.service;

import com.example.surveyserver.exception.ResourceNotFoundException;
import com.example.surveyserver.model.*;
import com.example.surveyserver.repository.SurveyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SurveyService {

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private SurveyReplyService surveyReplyService;

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

    public List<Survey> getAllSurveys() {
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
        return surveyRepository.findAll();
    }

    public Survey updateSurvey(Survey updatedSurvey) {
        Survey existingSurvey = surveyRepository.findById(updatedSurvey.getId())
                .orElseThrow(() -> new RuntimeException("Survey not found"));

        // Update the survey details
        existingSurvey.setTitle(updatedSurvey.getTitle());
        existingSurvey.setDescription(updatedSurvey.getDescription());
        existingSurvey.setAllowAnonymousReply(updatedSurvey.getAllowAnonymousReply());
        existingSurvey.setAllowResubmit(updatedSurvey.getAllowResubmit());
        existingSurvey.setStartTime(updatedSurvey.getStartTime());
        existingSurvey.setEndTime(updatedSurvey.getEndTime());
        existingSurvey.setMaxReplies(updatedSurvey.getMaxReplies());

        // Update the questions and options
        existingSurvey.setQuestions(updatedSurvey.getQuestions());

        // Set the survey reference for each question and update the options
        for (Question question : existingSurvey.getQuestions()) {
            question.setSurvey(existingSurvey);

            // Set the question reference for each option
            for (Option option : question.getOptions()) {
                option.setQuestion(question);
            }
        }

        // Save the updated survey
        surveyRepository.save(existingSurvey);

        return existingSurvey;
    }

    public Page<Survey> getSurveysByUser(Integer userId, Pageable pageable) {
        return surveyRepository.findByUserIdAndIsDeletedFalseOrderById(userId, pageable);
    }

    public Survey deleteSurvey(Integer id) {
        Survey survey = getSurvey(id);
        if (survey != null) {
            survey.setIsDeleted(true);
            return updateSurvey(survey);
        }
        return null;
    }

    public String generateRepliesCsvContent(Integer surveyId) {
        List<SurveyReply> surveyReplies = surveyReplyService.getRepliesBySurveyId(surveyId);
        Survey survey = getSurvey(surveyId);

        StringBuilder csvContent = new StringBuilder();

        // Add header row
        csvContent.append("Survey Reply ID");
        for (Question question : survey.getQuestions()) {
            csvContent.append(",").append(question.getQuestionText());
        }
        csvContent.append("\n");

        // Add data rows
        for (SurveyReply surveyReply : surveyReplies) {
            csvContent.append(surveyReply.getId());
            for (QuestionReply questionReply : surveyReply.getQuestionReplies()) {
                csvContent.append(",");
                Question.QuestionType questionType = Question.QuestionType.valueOf(questionReply.getQuestion().getQuestionType());
                switch (questionType) {
                    case TEXT:
                    case TEXTAREA:
                        csvContent.append(questionReply.getReplyText());
                        break;
                    case RADIO:
                    case CHECKBOX:
                        List<String> selectedOptions = questionReply.getOptionReplies().stream()
                                .filter(OptionReply::isSelected)
                                .map(optionReply -> optionReply.getOption().getOptionText())
                                .collect(Collectors.toList());
                        String optionTexts = String.join(", ", selectedOptions);
                        csvContent.append(optionTexts);
                        break;
                }
            }
            csvContent.append("\n");
        }

        return csvContent.toString();
    }

    public SurveySummary getSurveySummary(Integer surveyId) {
        Survey survey = getSurvey(surveyId);

        // Calculate survey summary
        List<SurveyReply> surveyReplies = surveyReplyService.getRepliesBySurveyId(surveyId);
        int totalReplies = surveyReplies.size();
        List<QuestionSummary> questionSummaries = new ArrayList<>();

        for (Question question : survey.getQuestions()) {
            Map<String, Integer> optionCounts = new HashMap<>();
            Map<String, Double> optionPercentages = new HashMap<>();

            for (Option option : question.getOptions()) {
                optionCounts.put(option.getOptionText(), 0);
            }

            for (SurveyReply reply : surveyReplies) {
                QuestionReply questionReply = reply.getQuestionReplies().stream()
                        .filter(qr -> qr.getQuestion().getId().equals(question.getId()))
                        .findFirst().orElse(null);

                if (questionReply != null) {
                    if (question.getQuestionType().equals(Question.QuestionType.RADIO.toString()) ||
                            question.getQuestionType().equals(Question.QuestionType.CHECKBOX.toString())) {
                        for (OptionReply optionReply : questionReply.getOptionReplies()) {
                            String optionText = optionReply.getOption().getOptionText();
                            optionCounts.put(optionText, optionCounts.get(optionText) + 1);
                        }
                    }
                }
            }

            for (String optionText : optionCounts.keySet()) {
                optionPercentages.put(optionText, (double) optionCounts.get(optionText) / totalReplies * 100);
            }

            questionSummaries.add(new QuestionSummary(question.getId(), question.getQuestionText(), optionCounts, optionPercentages));
        }

        return new SurveySummary(totalReplies, questionSummaries);
    }
}
