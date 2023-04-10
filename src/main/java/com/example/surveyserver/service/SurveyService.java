package com.example.surveyserver.service;

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
    private UserService userService;
    @Autowired
    private SurveyReplyService surveyReplyService;

    public Survey createSurvey(Survey survey, Integer userId) {
        User user = userService.getUser(userId);
        if (user == null) {
            return null;
        }
        survey.setUser(user);
        return surveyRepository.save(survey);
    }

    public Survey getSurvey(Integer id) {
        return surveyRepository.findById(id).orElse(null);
    }

    public List<Survey> getAllSurveys() {
        return surveyRepository.findAll();
    }

    public Survey updateSurvey(Survey survey) {
        return surveyRepository.save(survey);
    }

    public Page<Survey> getSurveysByUser(Integer userId, Pageable pageable) {
        return surveyRepository.findByUserIdOrderById(userId, pageable);
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
        Survey survey = surveyReplies.get(0).getSurvey();

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
        Survey survey = surveyRepository.findById(surveyId).orElse(null);
        if (survey == null) {
            return null;
        }

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
