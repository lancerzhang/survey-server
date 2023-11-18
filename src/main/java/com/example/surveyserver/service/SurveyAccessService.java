package com.example.surveyserver.service;

import com.example.surveyserver.model.*;
import com.example.surveyserver.oauth2.PrincipalUser;
import com.example.surveyserver.oauth2.PrincipalValidator;
import com.example.surveyserver.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SurveyAccessService {

    @Autowired
    private SurveyAccessRepository surveyAccessRepository;

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
}
