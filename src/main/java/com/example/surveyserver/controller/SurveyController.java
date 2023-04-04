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
import java.util.List;

@RestController
@RequestMapping("/api/surveys")
public class SurveyController {

    @Autowired
    private SurveyService surveyService;

    @PostMapping("/user/{userId}")
    public Survey createSurvey(@Valid @RequestBody Survey survey, @PathVariable Integer userId) {
        return surveyService.createSurvey(survey, userId);
    }

    @GetMapping("/{id}")
    public Survey getSurvey(@PathVariable Integer id) {
        return surveyService.getSurvey(id);
    }

    @GetMapping
    public List<Survey> getSurvey() {
        return surveyService.getAllSurveys();
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

    @GetMapping("/user/{userId}")
    public Page<Survey> getSurveysByUser(@PathVariable Integer userId,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size,
                                         @RequestParam(defaultValue = "createdAt") String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        return surveyService.getSurveysByUser(userId, pageable);
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


    @GetMapping("/{surveyId}/replies/csv")
    public ResponseEntity<Resource> downloadRepliesAsCsv(@PathVariable Integer surveyId) throws IOException {
        String csvContent = surveyService.generateRepliesCsvContent(surveyId);

        byte[] contentAsBytes = csvContent.getBytes(StandardCharsets.UTF_8);
        ByteArrayResource byteArrayResource = new ByteArrayResource(contentAsBytes);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentLength(contentAsBytes.length);
        headers.set("Content-Disposition", "attachment; filename=survey_replies.csv");

        return ResponseEntity.ok()
                .headers(headers)
                .body(byteArrayResource);
    }

    @GetMapping("/{surveyId}/summary")
    public SurveySummary getSurveySummary(@PathVariable Integer surveyId) {
        return surveyService.getSurveySummary(surveyId);
    }
}

