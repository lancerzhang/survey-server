package com.example.surveyserver.controller;

import com.example.surveyserver.model.SurveyReply;
import com.example.surveyserver.service.SurveyReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/survey-replies")
public class SurveyReplyController {

    @Autowired
    private SurveyReplyService surveyReplyService;

    @PostMapping
    public ResponseEntity<SurveyReply> createSurveyReply(@RequestBody SurveyReply surveyReply) {
        SurveyReply createdSurveyReply = surveyReplyService.createSurveyReply(surveyReply);
        return ResponseEntity.ok(createdSurveyReply);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SurveyReply> getSurveyReply(@PathVariable Integer id) {
        Optional<SurveyReply> surveyReply = surveyReplyService.getSurveyReply(id);
        return surveyReply.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SurveyReply> updateSurveyReply(@PathVariable Integer id, @RequestBody SurveyReply updatedSurveyReply) {
        Optional<SurveyReply> existingSurveyReply = surveyReplyService.getSurveyReply(id);
        if (existingSurveyReply.isPresent()) {
            updatedSurveyReply.setId(id);
            SurveyReply savedSurveyReply = surveyReplyService.updateSurveyReply(updatedSurveyReply);
            return ResponseEntity.ok(savedSurveyReply);
        }
        return ResponseEntity.notFound().build();
    }
}
