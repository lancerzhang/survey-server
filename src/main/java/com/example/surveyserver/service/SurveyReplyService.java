package com.example.surveyserver.service;

import com.example.surveyserver.exception.ResourceNotFoundException;
import com.example.surveyserver.model.*;
import com.example.surveyserver.repository.SurveyReplyRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
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

    public SurveyReply createSurveyReply(SurveyReply surveyReply, Survey survey) {

        // Validate the survey
        if (survey.getIsTemplate() || survey.getIsDeleted()) {
            throw new IllegalArgumentException("Cannot create a survey reply for a template or deleted survey.");
        }

        // Validate the start and end times
        LocalDateTime currentTime = LocalDateTime.now();
        if (survey.getStartTime() != null && currentTime.isBefore(survey.getStartTime().toLocalDateTime())) {
            throw new IllegalArgumentException("Cannot create a survey reply before the start time.");
        }
        if (survey.getEndTime() != null && currentTime.isAfter(survey.getEndTime().toLocalDateTime())) {
            throw new IllegalArgumentException("Cannot create a survey reply after the end time.");
        }

        // Validate the max replies
        if (survey.getMaxReplies() != null) {
            long currentReplyCount = surveyReplyRepository.countBySurveyId(survey.getId());
            if (currentReplyCount >= survey.getMaxReplies()) {
                throw new IllegalArgumentException("Cannot create a survey reply as the maximum reply limit has been reached.");
            }
        }

        // Set the isAnonymous property if it is true in the survey
        surveyReply.setIsAnonymous(survey.getIsAnonymous());

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
        Survey survey = surveyService.getSurvey(surveyReply.getSurvey().getId());

        if (survey.getIsTemplate() || survey.getIsDeleted() || !survey.getAllowResubmit()) {
            throw new RuntimeException("Cannot update survey reply due to survey restrictions");
        }

        // Validate the start and end times
        LocalDateTime currentTime = LocalDateTime.now();
        if (survey.getStartTime() != null && currentTime.isBefore(survey.getStartTime().toLocalDateTime())) {
            throw new RuntimeException("Cannot update survey reply before the survey start time");
        }

        if (survey.getEndTime() != null && currentTime.isAfter(survey.getEndTime().toLocalDateTime())) {
            throw new RuntimeException("Cannot update survey reply after the survey end time");
        }

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

    public String generateRepliesCsvContent(Integer surveyId) throws IOException {
        List<SurveyReply> surveyReplies = getRepliesBySurveyId(surveyId);
        Survey survey = surveyService.getSurvey(surveyId);

        Map<Integer, Question> questionMap = survey.getQuestions().stream()
                .collect(Collectors.toMap(Question::getId, Function.identity()));

        Map<Integer, Option> optionMap = survey.getQuestions().stream()
                .flatMap(question -> question.getOptions().stream())
                .collect(Collectors.toMap(Option::getId, Function.identity()));

        StringBuilder csvContent = new StringBuilder();
        CSVPrinter csvPrinter = new CSVPrinter(csvContent, CSVFormat.DEFAULT);

        // Add header row
        List<String> headers = new ArrayList<>();
        headers.add("Survey Reply ID");
        for (Question question : survey.getQuestions()) {
            headers.add(question.getQuestionText());
        }
        csvPrinter.printRecord(headers);

        // Add data rows
        for (SurveyReply surveyReply : surveyReplies) {
            List<String> row = new ArrayList<>();
            row.add(String.valueOf(surveyReply.getId()));
            if (surveyReply.getIsAnonymous()) {
                row.add("Anonymous ID");
                row.add("Anonymous Name");
            } else {
                row.add(surveyReply.getUser().getDisplayName());
            }
            for (QuestionReply questionReply : surveyReply.getQuestionReplies()) {
                Question question = questionMap.get(questionReply.getQuestionId());
                Question.QuestionType questionType = Question.QuestionType.valueOf(question.getQuestionType());
                switch (questionType) {
                    case TEXT:
                        row.add(questionReply.getReplyText());
                        break;
                    case CHOICE:
                        List<String> selectedOptions = questionReply.getOptionReplies().stream()
                                .filter(OptionReply::isSelected)
                                .map(optionReply -> {
                                    Option option = optionMap.get(optionReply.getOptionId());
                                    return option.getOptionText();
                                })
                                .collect(Collectors.toList());
                        String optionTexts = String.join(", ", selectedOptions);
                        row.add(optionTexts);
                        break;
                }
            }
            csvPrinter.printRecord(row);
        }

        return csvContent.toString();
    }

    public SurveyReplySummary getSurveySummary(Integer surveyId) {
        Survey survey = surveyService.getSurvey(surveyId);

        Map<Integer, Question> questionMap = survey.getQuestions().stream()
                .collect(Collectors.toMap(Question::getId, Function.identity()));

        Map<Integer, Option> optionMap = survey.getQuestions().stream()
                .flatMap(question -> question.getOptions().stream())
                .collect(Collectors.toMap(Option::getId, Function.identity()));

        // Calculate survey summary
        List<SurveyReply> surveyReplies = getRepliesBySurveyId(surveyId);
        int totalReplies = surveyReplies.size();
        List<QuestionReplySummary> questionSummaries = new ArrayList<>();

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
                    if (question.getQuestionType().equals(Question.QuestionType.CHOICE.toString())) {
                        for (OptionReply optionReply : questionReply.getOptionReplies()) {
                            if (optionReply.isSelected()) {
                                Option option = optionMap.get(optionReply.getOptionId());
                                String optionText = option.getOptionText();
                                optionCounts.put(optionText, optionCounts.get(optionText) + 1);
                            }
                        }
                    }
                }
            }

            for (String optionText : optionCounts.keySet()) {
                optionPercentages.put(optionText, (double) optionCounts.get(optionText) / totalReplies * 100);
            }

            questionSummaries.add(new QuestionReplySummary(question.getId(), question.getQuestionType(), question.getQuestionText(), optionCounts, optionPercentages));
        }

        return new SurveyReplySummary(survey.getTitle(), totalReplies, questionSummaries);
    }
}
