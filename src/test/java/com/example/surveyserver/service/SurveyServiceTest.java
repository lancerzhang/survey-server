package com.example.surveyserver.service;

import com.example.surveyserver.model.Survey;
import com.example.surveyserver.model.User;
import com.example.surveyserver.repository.SurveyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SurveyServiceTest {

    @InjectMocks
    private SurveyService surveyService;

    @Mock
    private SurveyRepository surveyRepository;

    @Mock
    private UserService userService;

    private Survey survey;
    private User user;

    @BeforeEach
    public void setUp() {
        survey = new Survey();
        survey.setId(1);

        user = new User();
        user.setId(1);
    }

    @Test
    public void testCreateSurvey() {
        when(userService.getUser(anyInt())).thenReturn(user);
        when(surveyRepository.save(any(Survey.class))).thenReturn(survey);

        Survey result = surveyService.createSurvey(survey, 1);

        assertEquals(survey, result);
        verify(userService, times(1)).getUser(1);
        verify(surveyRepository, times(1)).save(survey);
    }

    @Test
    public void testCreateSurveyWithInvalidUser() {
        when(userService.getUser(anyInt())).thenReturn(null);

        Survey result = surveyService.createSurvey(survey, 1);

        assertNull(result);
        verify(userService, times(1)).getUser(1);
    }

    @Test
    public void testGetSurvey() {
        when(surveyRepository.findById(anyInt())).thenReturn(Optional.of(survey));

        Survey result = surveyService.getSurvey(1);

        assertEquals(survey, result);
        verify(surveyRepository, times(1)).findById(1);
    }

    @Test
    public void testUpdateSurvey() {
        when(surveyRepository.save(any(Survey.class))).thenReturn(survey);

        Survey result = surveyService.updateSurvey(survey);

        assertEquals(survey, result);
        verify(surveyRepository, times(1)).save(survey);
    }

    @Test
    public void testGetSurveysByUser() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Survey> surveyPage = new PageImpl<>(Arrays.asList(survey), pageable, 1);
        when(surveyRepository.findByUserIdOrderById(anyInt(), any(Pageable.class))).thenReturn(surveyPage);

        Page<Survey> result = surveyService.getSurveysByUser(1, pageable);

        assertEquals(surveyPage, result);
        verify(surveyRepository, times(1)).findByUserIdOrderById(1, pageable);
    }
}
