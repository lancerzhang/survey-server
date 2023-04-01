package com.example.surveyserver.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import com.example.surveyserver.model.Survey;
import com.example.surveyserver.service.SurveyService;

@RestController
@RequestMapping("/api/surveys")
public class SurveyController {

    @Autowired
    private SurveyService surveyService;

    @PostMapping("/user/{userId}")
    public Survey createSurvey(@RequestBody Survey survey, @PathVariable Integer userId) {
        return surveyService.createSurvey(survey, userId);
    }

    @GetMapping("/{id}")
    public Survey getSurvey(@PathVariable Integer id) {
        return surveyService.getSurvey(id);
    }

    @PutMapping("/{id}")
    public Survey updateSurvey(@PathVariable Integer id, @RequestBody Survey updatedSurvey) {
        Survey survey = surveyService.getSurvey(id);
        if (survey == null) {
            return null;
        }
        updatedSurvey.setId(id);
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
}

