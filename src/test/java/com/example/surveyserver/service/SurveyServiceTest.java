package com.example.surveyserver.service;

import com.example.surveyserver.model.*;
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

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    private Survey survey;

    @BeforeEach
    public void setUp() {
        survey = new Survey();
        survey.setId(1);
        Question question1 = new Question();
        question1.setId(2);
        question1.setQuestionText("Question 1");

        Option option1 = new Option();
        option1.setId(3);
        option1.setOptionText("Option 1");

        question1.setOptions(Arrays.asList(option1));
        survey.setQuestions(Arrays.asList(question1));

    }

    @Test
    public void testCreateSurvey() {
        when(surveyRepository.save(any(Survey.class))).thenReturn(survey);

        Survey result = surveyService.createSurvey(survey);

        assertEquals(survey, result);
        verify(surveyRepository, times(1)).save(survey);
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
        when(surveyRepository.findById(1)).thenReturn(Optional.of(existingSurvey));
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
        verify(surveyRepository, times(1)).findById(1);
        verify(surveyRepository, times(1)).save(existingSurvey);
    }

    @Test
    public void testGetSurveysByUser() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Survey> surveyPage = new PageImpl<>(Arrays.asList(survey), pageable, 1);
        when(surveyRepository.findByUserIdAndIsTemplateFalseAndIsDeletedFalseOrderByIdDesc(anyInt(), any(Pageable.class))).thenReturn(surveyPage);

        Page<Survey> result = surveyService.getSurveysByUser(1, pageable);

        assertEquals(surveyPage, result);
        verify(surveyRepository, times(1)).findByUserIdAndIsTemplateFalseAndIsDeletedFalseOrderByIdDesc(1, pageable);
    }

    @Test
    public void testGenerateRepliesCsvContent() {
        // Set up mock data
        Integer surveyId = 1;
        List<SurveyReply> surveyReplies = new ArrayList<>();
        SurveyReply surveyReply = new SurveyReply();
        surveyReply.setId(1);
        Question question = new Question();
        question.setQuestionText("What is your name?");
        question.setQuestionType(Question.QuestionType.TEXT.name());
        QuestionReply questionReply = new QuestionReply();
        questionReply.setQuestion(question);
        questionReply.setReplyText("John");
        surveyReply.setQuestionReplies(Collections.singletonList(questionReply));
        surveyReplies.add(surveyReply);
        when(surveyReplyService.getRepliesBySurveyId(surveyId)).thenReturn(surveyReplies);
        Survey survey = new Survey();
        survey.setQuestions(Collections.singletonList(question));
        when(surveyRepository.findById(surveyId)).thenReturn(Optional.of(survey));

        // Call the method being tested
        String csvContent = surveyService.generateRepliesCsvContent(surveyId);

        // Verify the result
        String expectedCsvContent = "Survey Reply ID,What is your name?\n1,John\n";
        assertEquals(expectedCsvContent, csvContent);
    }
}
