package com.example.surveyserver.controller;

import com.example.surveyserver.model.Survey;
import com.example.surveyserver.model.SurveyAccess;
import com.example.surveyserver.model.SurveyAccess;
import com.example.surveyserver.oauth2.PrincipalValidator;
import com.example.surveyserver.service.SurveyAccessService;
import com.example.surveyserver.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/survey-access")
public class SurveyAccessController {

    @Autowired
    private SurveyAccessService surveyAccessService;
    @Autowired
    private SurveyService surveyService;

    @PostMapping
    public SurveyAccess addSurveyAccess(@Valid @RequestBody SurveyAccess surveyAccess, Authentication authentication) {
        Survey survey=surveyService.getSurvey(surveyAccess.getSurveyId());
        PrincipalValidator.validateUserPermission(survey.getUserId(), authentication);
        return surveyAccessService.addSurveyAccess(surveyAccess);
    }

    @GetMapping
    public List<SurveyAccess> getSurveyAccessSurveyId(@PathVariable Integer surveyId) {
        return surveyAccessService.getSurveyAccessSurveyId(surveyId);
    }

    @DeleteMapping("/{id}")
    public void removeSurveyAccess(@PathVariable Integer id, Authentication authentication) {
        surveyAccessService.removeSurveyAccess(id, authentication);
    }
}
