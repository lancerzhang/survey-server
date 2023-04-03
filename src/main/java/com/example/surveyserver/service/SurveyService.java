package com.example.surveyserver.service;

import com.example.surveyserver.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.surveyserver.model.Survey;
import com.example.surveyserver.repository.SurveyRepository;

import java.util.List;

@Service
public class SurveyService {

    @Autowired
    private SurveyRepository surveyRepository;
    @Autowired
    private UserService userService;

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
}
