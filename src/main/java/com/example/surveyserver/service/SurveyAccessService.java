package com.example.surveyserver.service;

import com.example.surveyserver.exception.ResourceNotFoundException;
import com.example.surveyserver.model.Survey;
import com.example.surveyserver.model.SurveyAccess;
import com.example.surveyserver.oauth2.PrincipalUser;
import com.example.surveyserver.oauth2.PrincipalValidator;
import com.example.surveyserver.repository.SurveyAccessRepository;
import com.example.surveyserver.repository.SurveyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SurveyAccessService {

    @Autowired
    private SurveyAccessRepository surveyAccessRepository;
    @Autowired
    private SurveyRepository surveyRepository;

    public void validateSurveyAccess(Survey survey, Authentication authentication) {
        PrincipalUser authenticatedUser = (PrincipalUser) authentication.getPrincipal();

        // Check if the survey is created by the user or their delegate
        if (!authenticatedUser.getDelegators().contains(survey.getUserId())) {
            // Check if the user has access rights to the survey in the survey_access table
            boolean hasAccess = surveyAccessRepository.existsBySurveyIdAndUserId(survey.getId(), authenticatedUser.getId());
            if (!hasAccess) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to perform this action");
            }
        }
    }

    public SurveyAccess addSurveyAccess(SurveyAccess surveyAccess) {
        return surveyAccessRepository.save(surveyAccess);
    }

    public List<SurveyAccess> getSurveyAccessSurveyId(Integer surveyId) {
        return surveyAccessRepository.findBySurveyId(surveyId);
    }

    public void removeSurveyAccess(Integer id, Authentication authentication) {
        // Fetch the surveyAccess from the repository
        SurveyAccess surveyAccess = surveyAccessRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("SurveyAccess not found with ID: " + id));
        Survey survey = surveyRepository.findByIdAndIsDeletedFalse(surveyAccess.getSurveyId()).orElseThrow(() -> new ResourceNotFoundException("Survey not found with ID: " + id));
        // Validate if the authenticated user has the right to remove the surveyAccess
        PrincipalValidator.validateUserPermission(survey.getUserId(), authentication);

        surveyAccessRepository.deleteById(id);
    }
}
