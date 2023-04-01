package com.example.surveyserver.service;

import com.example.surveyserver.model.SurveyReply;
import com.example.surveyserver.repository.SurveyReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
}
