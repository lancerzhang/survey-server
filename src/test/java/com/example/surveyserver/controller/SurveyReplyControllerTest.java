package com.example.surveyserver.controller;

import com.example.surveyserver.model.SurveyReply;
import com.example.surveyserver.service.SurveyReplyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SurveyReplyControllerTest {

    @InjectMocks
    private SurveyReplyController surveyReplyController;

    @Mock
    private SurveyReplyService surveyReplyService;

    private SurveyReply surveyReply;

    @BeforeEach
    public void setUp() {
        surveyReply = new SurveyReply();
        surveyReply.setId(1);
    }

    @Test
    public void testCreateSurveyReply() {
        when(surveyReplyService.createSurveyReply(any(SurveyReply.class))).thenReturn(surveyReply);

        ResponseEntity<SurveyReply> response = surveyReplyController.createSurveyReply(surveyReply);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(surveyReply, response.getBody());
        verify(surveyReplyService, times(1)).createSurveyReply(surveyReply);
    }

    @Test
    public void testGetSurveyReply() {
        when(surveyReplyService.getSurveyReply(anyInt())).thenReturn(Optional.of(surveyReply));

        ResponseEntity<SurveyReply> response = surveyReplyController.getSurveyReply(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(surveyReply, response.getBody());
        verify(surveyReplyService, times(1)).getSurveyReply(1);
    }

    @Test
    public void testGetSurveyReplyNotFound() {
        when(surveyReplyService.getSurveyReply(anyInt())).thenReturn(Optional.empty());

        ResponseEntity<SurveyReply> response = surveyReplyController.getSurveyReply(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(surveyReplyService, times(1)).getSurveyReply(1);
    }

    @Test
    public void testUpdateSurveyReply() {
        when(surveyReplyService.getSurveyReply(anyInt())).thenReturn(Optional.of(surveyReply));
        when(surveyReplyService.updateSurveyReply(any(SurveyReply.class))).thenReturn(surveyReply);

        ResponseEntity<SurveyReply> response = surveyReplyController.updateSurveyReply(1, surveyReply);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(surveyReply, response.getBody());
        verify(surveyReplyService, times(1)).getSurveyReply(1);
        verify(surveyReplyService, times(1)).updateSurveyReply(surveyReply);
    }

    @Test
    public void testUpdateSurveyReplyNotFound() {
        when(surveyReplyService.getSurveyReply(anyInt())).thenReturn(Optional.empty());

        ResponseEntity<SurveyReply> response = surveyReplyController.updateSurveyReply(1, surveyReply);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(surveyReplyService, times(1)).getSurveyReply(1);
    }
}
