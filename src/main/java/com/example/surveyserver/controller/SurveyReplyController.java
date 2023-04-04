package com.example.surveyserver.controller;

import com.example.surveyserver.model.SurveyReply;
import com.example.surveyserver.service.SurveyReplyService;
import com.example.surveyserver.utils.CsvUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/survey-replies")
public class SurveyReplyController {

    @Autowired
    private SurveyReplyService surveyReplyService;

    @PostMapping
    public ResponseEntity<SurveyReply> createSurveyReply(@Valid @RequestBody SurveyReply surveyReply) {
        SurveyReply createdSurveyReply = surveyReplyService.createSurveyReply(surveyReply);
        return ResponseEntity.ok(createdSurveyReply);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SurveyReply> getSurveyReply(@PathVariable Integer id) {
        Optional<SurveyReply> surveyReply = surveyReplyService.getSurveyReply(id);
        return surveyReply.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SurveyReply> updateSurveyReply(@PathVariable Integer id, @Valid @RequestBody SurveyReply updatedSurveyReply) {
        Optional<SurveyReply> existingSurveyReply = surveyReplyService.getSurveyReply(id);
        if (existingSurveyReply.isPresent()) {
            updatedSurveyReply.setId(id);
            SurveyReply savedSurveyReply = surveyReplyService.updateSurveyReply(updatedSurveyReply);
            return ResponseEntity.ok(savedSurveyReply);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/user/{userId}")
    public Page<SurveyReply> getRepliesByUser(@PathVariable Integer userId, Pageable pageable) {
        return surveyReplyService.getRepliesByUser(userId, pageable);
    }

    @GetMapping("/survey/{surveyId}/download")
    public void downloadRepliesCsv(@PathVariable Integer surveyId, HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=survey_replies_" + surveyId + ".csv");
        List<SurveyReply> surveyReplies = surveyReplyService.getRepliesBySurveyId(surveyId);
        try (PrintWriter writer = response.getWriter()) {
            CsvUtil.surveyRepliesToCsv(surveyReplies, writer);
        }
    }
}
