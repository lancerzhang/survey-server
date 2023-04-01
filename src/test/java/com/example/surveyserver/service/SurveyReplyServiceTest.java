package com.example.surveyserver.service;

import com.example.surveyserver.model.SurveyReply;
import com.example.surveyserver.repository.SurveyReplyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SurveyReplyServiceTest {

    @InjectMocks
    private SurveyReplyService surveyReplyService;

    @Mock
    private SurveyReplyRepository surveyReplyRepository;

    private SurveyReply surveyReply;

    @BeforeEach
    public void setUp() {
        surveyReply = new SurveyReply();
        surveyReply.setId(1);
    }

    @Test
    public void testCreateSurveyReply() {
        when(surveyReplyRepository.save(any(SurveyReply.class))).thenReturn(surveyReply);

        SurveyReply result = surveyReplyService.createSurveyReply(surveyReply);

        assertEquals(surveyReply, result);
        verify(surveyReplyRepository, times(1)).save(surveyReply);
    }

    @Test
    public void testGetSurveyReply() {
        when(surveyReplyRepository.findById(anyInt())).thenReturn(Optional.of(surveyReply));

        Optional<SurveyReply> result = surveyReplyService.getSurveyReply(1);

        assertEquals(Optional.of(surveyReply), result);
        verify(surveyReplyRepository, times(1)).findById(1);
    }

    @Test
    public void testUpdateSurveyReply() {
        when(surveyReplyRepository.save(any(SurveyReply.class))).thenReturn(surveyReply);

        SurveyReply result = surveyReplyService.updateSurveyReply(surveyReply);

        assertEquals(surveyReply, result);
        verify(surveyReplyRepository, times(1)).save(surveyReply);
    }
}
