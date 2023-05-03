package com.example.surveyserver.controller;

import com.example.surveyserver.model.Survey;
import com.example.surveyserver.model.SurveySummary;
import com.example.surveyserver.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/templates")
public class TemplateController {

    @Autowired
    private SurveyService surveyService;

    @PostMapping
    public Survey createSurvey(@Valid @RequestBody Survey survey) {
        return surveyService.createTemplate(survey);
    }

    @GetMapping
    public Page<Survey> getTemplates(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   @RequestParam(defaultValue = "createdAt") String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        return surveyService.getTemplates(pageable);
    }

    @GetMapping("/{id}")
    public Survey getSurvey(@PathVariable int id) {
        return surveyService.getSurvey(id);
    }

    @PutMapping("/{id}")
    public Survey updateSurvey(@PathVariable Integer id, @Valid @RequestBody Survey updatedSurvey) {
        Survey survey = surveyService.getSurvey(id);
        if (survey == null) {
            return null;
        }
        updatedSurvey.setId(id);
        return surveyService.updateSurvey(updatedSurvey);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Survey> deleteSurvey(@PathVariable Integer id) {
        Survey deletedSurvey = surveyService.deleteSurvey(id);
        if (deletedSurvey != null) {
            return ResponseEntity.ok(deletedSurvey);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

