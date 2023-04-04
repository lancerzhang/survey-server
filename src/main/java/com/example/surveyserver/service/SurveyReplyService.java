package com.example.surveyserver.service;

import com.example.surveyserver.model.*;
import com.example.surveyserver.repository.SurveyReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SurveyReplyService {

    @Autowired
    private SurveyReplyRepository surveyReplyRepository;

    public SurveyReply createSurveyReply(SurveyReply surveyReply) {
        return surveyReplyRepository.save(surveyReply);
    }

    public Optional<SurveyReply> getSurveyReply(Integer id) {
        return surveyReplyRepository.findById(id);
    }

    public SurveyReply updateSurveyReply(SurveyReply surveyReply) {
        return surveyReplyRepository.save(surveyReply);
    }

    public Page<SurveyReply> getRepliesByUser(Integer userId, Pageable pageable) {
        return surveyReplyRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    public List<SurveyReply> getRepliesBySurveyId(Integer surveyId) {
        return surveyReplyRepository.findBySurveyId(surveyId);
    }

    public String generateRepliesCsvContent(Integer surveyId) {
        List<SurveyReply> surveyReplies = getRepliesBySurveyId(surveyId);
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
            for (QuestionReply questionReply : surveyReply.getQuestionReply()) {
                csvContent.append(",");
                Question.QuestionType questionType = Question.QuestionType.valueOf(questionReply.getQuestion().getQuestionType());
                switch (questionType) {
                    case TEXT:
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
}
