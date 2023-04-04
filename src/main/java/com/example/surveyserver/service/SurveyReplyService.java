package com.example.surveyserver.service;

import com.example.surveyserver.model.SurveyReply;
import com.example.surveyserver.repository.SurveyReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public Page<SurveyReply> getRepliesByUser(Integer userId, Pageable pageable) {
        return surveyReplyRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    public List<SurveyReply> getRepliesBySurveyId(Integer surveyId) {
        return surveyReplyRepository.findBySurveyId(surveyId);
    }
}
