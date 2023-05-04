package com.example.surveyserver.controller;

import com.example.surveyserver.model.SurveyReply;
import com.example.surveyserver.model.SurveySummary;
import com.example.surveyserver.service.SurveyReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/survey-replies")
public class SurveyReplyController {

    @Autowired
    private SurveyReplyService surveyReplyService;

    @PostMapping
    public SurveyReply createSurveyReply(@Valid @RequestBody SurveyReply surveyReply) {
        SurveyReply createdSurveyReply = surveyReplyService.createSurveyReply(surveyReply);
        return createdSurveyReply;
    }

    @GetMapping("/{id}")
    public SurveyReply getSurveyReply(@PathVariable Integer id) {
        return surveyReplyService.getSurveyReply(id);
    }

    @PutMapping("/{id}")
    public SurveyReply updateSurveyReply(@PathVariable Integer id, @Valid @RequestBody SurveyReply updatedSurveyReply) {
        SurveyReply existingSurveyReply = surveyReplyService.getSurveyReply(id);
        updatedSurveyReply.setId(existingSurveyReply.getId());
        return surveyReplyService.updateSurveyReply(updatedSurveyReply);
    }

    @GetMapping("/user/{userId}")
    public Page<SurveyReply> getRepliesByUser(@PathVariable Integer userId, Pageable pageable) {
        return surveyReplyService.getRepliesByUser(userId, pageable);
    }

    @GetMapping("/surveys/{surveyId}/user/{userId}")
    public SurveyReply getSurveyReplyBySurveyIdAndUserId(@PathVariable Integer surveyId, @PathVariable Integer userId) {
        return surveyReplyService.getSurveyReplyBySurveyIdAndUserId(surveyId, userId);
    }

    @GetMapping("/surveys/{surveyId}/csv")
    public ResponseEntity<Resource> downloadRepliesAsCsv(@PathVariable Integer surveyId) throws IOException {
        String csvContent = surveyReplyService.generateRepliesCsvContent(surveyId);

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

    @GetMapping("/surveys/{surveyId}/summary")
    public SurveySummary getSurveySummary(@PathVariable Integer surveyId) {
        return surveyReplyService.getSurveySummary(surveyId);
    }
}

