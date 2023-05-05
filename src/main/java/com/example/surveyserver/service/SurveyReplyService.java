package com.example.surveyserver.service;

import com.example.surveyserver.exception.ResourceNotFoundException;
import com.example.surveyserver.model.*;
import com.example.surveyserver.repository.SurveyReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SurveyReplyService {

    @Autowired
    private SurveyReplyRepository surveyReplyRepository;

    @Autowired
    private SurveyService surveyService;

    public SurveyReply createSurveyReply(SurveyReply surveyReply) {
        List<QuestionReply> questionReplies = surveyReply.getQuestionReplies();
        questionReplies.forEach(questionReply -> {
            // bidirectional association to reduce sql statements
            // https://vladmihalcea.com/the-best-way-to-map-a-onetomany-association-with-jpa-and-hibernate/
            questionReply.setSurveyReply(surveyReply);
            List<OptionReply> optionReplies = questionReply.getOptionReplies();
            if (optionReplies != null) {
                optionReplies.forEach(optionReply -> {
                    optionReply.setQuestionReply(questionReply);
                });
            }
        });
        return surveyReplyRepository.save(surveyReply);
    }

    public SurveyReply getSurveyReply(Integer id) {
        return surveyReplyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Survey reply not found with ID: " + id));
    }

    public SurveyReply getSurveyReplyBySurveyIdAndUserId(Integer surveyId, Integer userId) {
        return surveyReplyRepository.findBySurveyIdAndUserId(surveyId, userId).orElseThrow(() -> new ResourceNotFoundException("Survey reply not found."));
    }

    // get all replies by survey id with pagination
    public Page<SurveyReply> getAllRepliesBySurveyId(Integer surveyId, Pageable pageable) {
        return surveyReplyRepository.findBySurveyIdOrderByCreatedAtDesc(surveyId, pageable);
    }

    public long countSurveyRepliesBySurveyId(Integer surveyId) {
        return surveyReplyRepository.countBySurveyId(surveyId);
    }

    public SurveyReply updateSurveyReply(SurveyReply surveyReply) {
        SurveyReply existingSurveyReply = surveyReplyRepository.findById(surveyReply.getId())
                .orElseThrow(() -> new RuntimeException("Survey reply not found"));

        // Update the questions and options
        existingSurveyReply.setQuestionReplies(surveyReply.getQuestionReplies());

        // Set the survey reference for each question and update the options
        for (QuestionReply questionReply : existingSurveyReply.getQuestionReplies()) {
            questionReply.setSurveyReply(existingSurveyReply);

            // Set the question reference for each option
            List<OptionReply> optionReplies = questionReply.getOptionReplies();
            if (optionReplies != null) {
                for (OptionReply optionReply : optionReplies) {
                    optionReply.setQuestionReply(questionReply);
                }
            }
        }

        // Save the updated survey
        return surveyReplyRepository.save(existingSurveyReply);
    }

    public Page<SurveyReply> getRepliesByUser(Integer userId, Pageable pageable) {
        return surveyReplyRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    public List<SurveyReply> getRepliesBySurveyId(Integer surveyId) {
        return surveyReplyRepository.findBySurveyId(surveyId);
    }


    public String generateRepliesCsvContent(Integer surveyId) {
        List<SurveyReply> surveyReplies = getRepliesBySurveyId(surveyId);
        Survey survey = surveyService.getSurvey(surveyId);

        Map<Integer, Question> questionMap = survey.getQuestions().stream()
                .collect(Collectors.toMap(Question::getId, Function.identity()));

        Map<Integer, Option> optionMap = survey.getQuestions().stream()
                .flatMap(question -> question.getOptions().stream())
                .collect(Collectors.toMap(Option::getId, Function.identity()));

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
                Question question = questionMap.get(questionReply.getQuestionId());
                Question.QuestionType questionType = Question.QuestionType.valueOf(question.getQuestionType());
                switch (questionType) {
                    case TEXT:
                        csvContent.append(questionReply.getReplyText());
                        break;
                    case RADIO:
                    case CHECKBOX:
                        List<String> selectedOptions = questionReply.getOptionReplies().stream()
                                .filter(OptionReply::isSelected)
                                .map(optionReply -> {
                                    Option option = optionMap.get(optionReply.getOptionId());
                                    return option.getOptionText();
                                })
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
        Survey survey = surveyService.getSurvey(surveyId);

        Map<Integer, Question> questionMap = survey.getQuestions().stream()
                .collect(Collectors.toMap(Question::getId, Function.identity()));

        Map<Integer, Option> optionMap = survey.getQuestions().stream()
                .flatMap(question -> question.getOptions().stream())
                .collect(Collectors.toMap(Option::getId, Function.identity()));

        // Calculate survey summary
        List<SurveyReply> surveyReplies = getRepliesBySurveyId(surveyId);
        int totalReplies = surveyReplies.size();
        List<QuestionSummary> questionSummaries = new ArrayList<>();

        for (Question question : survey.getQuestions()) {
            Map<String, Integer> optionCounts = new HashMap<>();
            Map<String, Double> optionPercentages = new HashMap<>();

            for (Option option : question.getOptions()) {
                optionCounts.put(option.getOptionText(), 0);
            }

            for (SurveyReply surveyReply : surveyReplies) {
                QuestionReply questionReply = surveyReply.getQuestionReplies().stream()
                        .filter(qr -> qr.getQuestionId().equals(question.getId()))
                        .findFirst().orElse(null);

                if (questionReply != null) {
                    if (question.getQuestionType().equals(Question.QuestionType.RADIO.toString()) ||
                            question.getQuestionType().equals(Question.QuestionType.CHECKBOX.toString())) {
                        for (OptionReply optionReply : questionReply.getOptionReplies()) {
                            Option option = optionMap.get(optionReply.getOptionId());
                            String optionText = option.getOptionText();
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
