package com.example.surveyserver.controller;

import com.example.surveyserver.model.Survey;
import com.example.surveyserver.oauth2.PrincipalValidator;
import com.example.surveyserver.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/surveys")
public class SurveyController {

    @Autowired
    private SurveyService surveyService;

    @PostMapping
    public Survey createSurvey(@Valid @RequestBody Survey survey, Authentication authentication) {
        PrincipalValidator.validateUserPermission(survey.getUserId(), authentication);
        return surveyService.createSurvey(survey);
    }

    @GetMapping("/{id}")
    public Survey getSurvey(@PathVariable int id) {
        return surveyService.getSurvey(id);
    }

    @GetMapping
    public Page<Survey> getSurveys(@RequestParam(defaultValue = "false") boolean isTemplate,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   @RequestParam(defaultValue = "createdAt") String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        if (isTemplate) {
            return surveyService.getAllTemplates(pageable);
        } else {
            return surveyService.getAllSurveys(pageable);
        }
    }

    @PutMapping("/{id}")
    public Survey updateSurvey(@PathVariable Integer id, @Valid @RequestBody Survey updatedSurvey, Authentication authentication) {
        PrincipalValidator.validateUserPermission(updatedSurvey.getUserId(), authentication);
        Survey survey = surveyService.getSurvey(id);
        updatedSurvey.setId(survey.getId());
        return surveyService.updateSurvey(updatedSurvey);
    }

    @GetMapping("/user/{userId}")
    public Page<Survey> getSurveysByUser(@PathVariable Integer userId,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size,
                                         @RequestParam(defaultValue = "createdAt") String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        return surveyService.getSurveysByUser(userId, pageable);
    }

    @GetMapping("/replied/user/{userId}")
    public Page<Survey> getRepliedSurveysByUser(@PathVariable Integer userId, Pageable pageable) {
        return surveyService.getRepliedSurveysByUser(userId, pageable);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Survey> deleteSurvey(@PathVariable Integer id, Authentication authentication) {
        Survey deletedSurvey = surveyService.deleteSurvey(id, authentication);
        if (deletedSurvey != null) {
            return ResponseEntity.ok(deletedSurvey);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

