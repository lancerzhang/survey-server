package com.example.surveyserver.service;

import com.example.surveyserver.exception.ResourceNotFoundException;
import com.example.surveyserver.model.*;
import com.example.surveyserver.oauth2.PrincipalValidator;
import com.example.surveyserver.repository.SurveyReplyRepository;
import com.example.surveyserver.repository.SurveyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SurveyServiceTest {

    @InjectMocks
    private SurveyService surveyService;
    @Mock
    private SurveyReplyService surveyReplyService;
    @Mock
    private SurveyRepository surveyRepository;
    @Mock
    private SurveyReplyRepository surveyReplyRepository;
    private Survey survey;
    private SurveyReply surveyReply;

    @BeforeEach
    public void setUp() {

        User user = new User();
        user.setId(1);

        survey = new Survey();
        survey.setId(1);
        survey.setTitle("Test Survey");
        survey.setDescription("This is a test survey.");
        Question question1 = new Question();
        question1.setId(2);
        question1.setQuestionText("Question 1");

        Option option1 = new Option();
        option1.setId(3);
        option1.setOptionText("Option 1");

        question1.setOptions(Arrays.asList(option1));
        survey.setQuestions(Arrays.asList(question1));

        surveyReply = new SurveyReply();
        surveyReply.setId(1);
        surveyReply.setUser(user);
        surveyReply.setSurvey(survey);

    }

    @Test
    public void testCreateSurvey() {
        when(surveyRepository.save(any(Survey.class))).thenReturn(survey);

        Survey result = surveyService.createSurvey(survey);

        assertEquals(survey, result);
        verify(surveyRepository, times(1)).save(survey);
    }

    @Test
    public void testUpdateSurvey() {
        // Create the updated survey
        Survey updatedSurvey = new Survey();
        updatedSurvey.setId(1);
        updatedSurvey.setTitle("Updated Title");
        updatedSurvey.setDescription("Updated Description");

        Question question1 = new Question();
        question1.setId(1);
        question1.setQuestionText("Question 1 Text");

        Option option1 = new Option();
        option1.setId(1);
        option1.setOptionText("Option 1");

        question1.setOptions(Arrays.asList(option1));
        updatedSurvey.setQuestions(Arrays.asList(question1));

        // Create the existing survey
        Survey existingSurvey = new Survey();
        existingSurvey.setId(1);
        existingSurvey.setTitle("Original Title");
        existingSurvey.setDescription("Original Description");

        Question existingQuestion1 = new Question();
        existingQuestion1.setId(1);
        existingQuestion1.setQuestionText("Question 1 Text");

        Option existingOption1 = new Option();
        existingOption1.setId(1);
        existingOption1.setOptionText("Option 1");

        existingQuestion1.setOptions(Arrays.asList(existingOption1));
        existingSurvey.setQuestions(Arrays.asList(existingQuestion1));

        // Set up mock repository behavior
        when(surveyRepository.findByIdAndIsDeletedFalse(1)).thenReturn(Optional.of(existingSurvey));
        when(surveyRepository.save(existingSurvey)).thenReturn(existingSurvey);

        // Call the method to update the survey
        Survey result = surveyService.updateSurvey(updatedSurvey);

        // Verify that the existing survey was updated correctly
        assertEquals("Updated Title", existingSurvey.getTitle());
        assertEquals("Updated Description", existingSurvey.getDescription());

        Question updatedQuestion1 = existingSurvey.getQuestions().get(0);
        assertEquals("Question 1 Text", updatedQuestion1.getQuestionText());

        Option updatedOption1 = updatedQuestion1.getOptions().get(0);
        assertEquals("Option 1", updatedOption1.getOptionText());

        // Verify that the method returned the existing survey
        assertEquals(existingSurvey, result);

        // Verify that the repository methods were called as expected
        verify(surveyRepository, times(1)).findByIdAndIsDeletedFalse(1);
        verify(surveyRepository, times(1)).save(existingSurvey);
    }

    @Test
    public void testGetSurvey() {
        when(surveyRepository.findByIdAndIsDeletedFalse(1)).thenReturn(Optional.of(survey));

        Survey result = surveyService.getSurvey(1);
        assertEquals(survey, result);
    }

    @Test
    public void testGetSurveyNotFound() {
        when(surveyRepository.findByIdAndIsDeletedFalse(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> surveyService.getSurvey(1));
    }

    @Test
    public void testGetAllSurveys() {
        Page<Survey> surveyPage = new PageImpl<>(Arrays.asList(survey));
        when(surveyRepository.findByIsTemplateFalseAndIsDeletedFalseOrderByIdDesc(any(Pageable.class))).thenReturn(surveyPage);

        Page<Survey> result = surveyService.getAllSurveys(PageRequest.of(0, 10));
        assertEquals(surveyPage, result);
    }

    @Test
    public void testGetRepliedSurveysByUser() {
        Page<SurveyReply> surveyReplyPage = new PageImpl<>(Arrays.asList(surveyReply));
        when(surveyReplyRepository.findByUserIdOrderByCreatedAtDesc(eq(1), any(Pageable.class))).thenReturn(surveyReplyPage);

        Page<Survey> result = surveyService.getRepliedSurveysByUser(1, PageRequest.of(0, 10));
        assertEquals(1, result.getContent().size());
        assertEquals(survey, result.getContent().get(0));
    }

    @Test
    public void testGetAllTemplates() {
        Page<Survey> surveyPage = new PageImpl<>(Arrays.asList(survey));
        when(surveyRepository.findByIsTemplateTrueAndIsDeletedFalseOrderByIdDesc(any(Pageable.class))).thenReturn(surveyPage);

        Page<Survey> result = surveyService.getAllTemplates(PageRequest.of(0, 10));
        assertEquals(surveyPage, result);
    }

    @Test
    public void testDeleteSurvey() {
        when(surveyRepository.findByIdAndIsDeletedFalse(1)).thenReturn(Optional.of(survey));
        when(surveyRepository.save(any(Survey.class))).thenReturn(survey);

        try (MockedStatic<PrincipalValidator> mocked = Mockito.mockStatic(PrincipalValidator.class)) {
            mocked.when(() -> PrincipalValidator.validateUserPermission(anyInt(), any())).thenAnswer(i -> null);

            Survey deletedSurvey = surveyService.deleteSurvey(1, null);

            assertTrue(deletedSurvey.getIsDeleted());
            verify(surveyRepository, times(1)).save(deletedSurvey);
        }
    }

}

