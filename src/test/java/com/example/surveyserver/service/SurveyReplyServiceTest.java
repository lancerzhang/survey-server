package com.example.surveyserver.service;

import com.example.surveyserver.exception.ResourceNotFoundException;
import com.example.surveyserver.model.*;
import com.example.surveyserver.repository.SurveyReplyRepository;
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
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SurveyReplyServiceTest {

    @Mock
    private SurveyService surveyService;

    @InjectMocks
    private SurveyReplyService surveyReplyService;

    @Mock
    private SurveyReplyRepository surveyReplyRepository;

    private Survey survey;

    private SurveyReply surveyReply;

    @BeforeEach
    public void setUp() {

        survey = new Survey();
        survey.setId(1);
        survey.setTitle("Test Survey");
        survey.setDescription("This is a test survey.");
        Question question1 = new Question();
        question1.setId(2);
        question1.setQuestionType(String.valueOf(Question.QuestionType.RADIO));
        question1.setQuestionText("Question 1");

        Option option1 = new Option();
        option1.setId(3);
        option1.setOptionText("Option 1");

        question1.setOptions(Arrays.asList(option1));
        survey.setQuestions(Arrays.asList(question1));


        surveyReply = new SurveyReply();
        surveyReply.setId(1);
        surveyReply.setUserId(1);
        surveyReply.setIsAnonymous(false);
        surveyReply.setCreatedAt(new Date());
        surveyReply.setLastModified(new Date());

        QuestionReply questionReply = new QuestionReply();
        questionReply.setId(1);
        questionReply.setQuestionId(2);
        questionReply.setSurveyReply(surveyReply);

        OptionReply optionReply = new OptionReply();
        optionReply.setId(1);
        optionReply.setOptionId(3);
        optionReply.setQuestionReply(questionReply);
        questionReply.setOptionReplies(Arrays.asList(optionReply));

        surveyReply.setQuestionReplies(Arrays.asList(questionReply));
    }

    @Test
    public void testCreateSurveyReply() {
        when(surveyReplyRepository.save(any(SurveyReply.class))).thenReturn(surveyReply);

        SurveyReply createdSurveyReply = surveyReplyService.createSurveyReply(surveyReply);

        assertNotNull(createdSurveyReply);
        assertEquals(surveyReply, createdSurveyReply);
        verify(surveyReplyRepository, times(1)).save(surveyReply);
    }

    @Test
    public void testGetSurveyReply() {
        when(surveyReplyRepository.findById(1)).thenReturn(Optional.of(surveyReply));

        SurveyReply foundSurveyReply = surveyReplyService.getSurveyReply(1);

        assertNotNull(foundSurveyReply);
        assertEquals(surveyReply, foundSurveyReply);
    }

    @Test
    public void testGetSurveyReplyNotFound() {
        when(surveyReplyRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> surveyReplyService.getSurveyReply(1));
    }

    @Test
    public void testGetAllRepliesBySurveyId() {
        List<SurveyReply> surveyReplies = Arrays.asList(surveyReply);
        Page<SurveyReply> page = new PageImpl<>(surveyReplies);

        when(surveyReplyRepository.findBySurveyIdOrderByCreatedAtDesc(anyInt(), any(Pageable.class))).thenReturn(page);

        Pageable pageable = PageRequest.of(0, 10);
        Page<SurveyReply> result = surveyReplyService.getAllRepliesBySurveyId(1, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(surveyReply, result.getContent().get(0));
    }

    @Test
    public void testCountSurveyRepliesBySurveyId() {
        when(surveyReplyRepository.countBySurveyId(1)).thenReturn(1L);

        long count = surveyReplyService.countSurveyRepliesBySurveyId(1);

        assertEquals(1L, count);
    }

    @Test
    public void testUpdateSurveyReply() {
        SurveyReply updatedSurveyReply = new SurveyReply();
        updatedSurveyReply.setId(1);
        updatedSurveyReply.setUserId(1);
        updatedSurveyReply.setIsAnonymous(false);
        updatedSurveyReply.setCreatedAt(new Date());
        updatedSurveyReply.setLastModified(new Date());

        QuestionReply questionReply = new QuestionReply();
        questionReply.setId(2);
        questionReply.setQuestionId(1);
        questionReply.setSurveyReply(updatedSurveyReply);

        updatedSurveyReply.setQuestionReplies(Arrays.asList(questionReply));

        when(surveyReplyRepository.findById(1)).thenReturn(Optional.of(surveyReply));
        when(surveyReplyRepository.save(surveyReply)).thenReturn(updatedSurveyReply);

        SurveyReply result = surveyReplyService.updateSurveyReply(updatedSurveyReply);

        assertNotNull(result);
        assertEquals(updatedSurveyReply, result);
    }

    @Test
    public void testGetRepliesByUser() {
        List<SurveyReply> surveyReplies = Arrays.asList(surveyReply);
        Page<SurveyReply> page = new PageImpl<>(surveyReplies);

        when(surveyReplyRepository.findByUserIdOrderByCreatedAtDesc(anyInt(), any(Pageable.class))).thenReturn(page);

        Pageable pageable = PageRequest.of(0, 10);
        Page<SurveyReply> result = surveyReplyService.getRepliesByUser(1, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(surveyReply, result.getContent().get(0));
    }

    @Test
    public void testGetRepliesBySurveyId() {
        List<SurveyReply> surveyReplies = Arrays.asList(surveyReply);

        when(surveyReplyRepository.findBySurveyId(1)).thenReturn(surveyReplies);

        List<SurveyReply> result = surveyReplyService.getRepliesBySurveyId(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(surveyReply, result.get(0));
    }

    @Test
    public void getSurveySummaryTest() {

        List<SurveyReply> surveyReplies = Arrays.asList(surveyReply);

        // Mock the dependencies
        when(surveyService.getSurvey(1)).thenReturn(survey);
        when(surveyReplyRepository.findBySurveyId(1)).thenReturn(surveyReplies);

        // Call the method to be tested
        SurveyReplySummary summary = surveyReplyService.getSurveySummary(1);

        // Verify the results
        assertEquals("Test Survey", summary.getSurveyTitle());
        assertEquals(1, summary.getTotalReplies());
        assertEquals(1, summary.getQuestionSummaries().size());
        // Add more assertions to verify the content of summary.getQuestionSummaries() if needed
    }

    @Test
    public void generateRepliesCsvContentTest() {
        // Prepare sample data

        List<SurveyReply> surveyReplies = Arrays.asList(surveyReply);

        // Mock the dependencies
        when(surveyService.getSurvey(1)).thenReturn(survey);
        when(surveyReplyRepository.findBySurveyId(1)).thenReturn(surveyReplies);

        // Call the method to be tested
        String csvContent = surveyReplyService.generateRepliesCsvContent(1);

        // Verify the results
        String[] csvLines = csvContent.split("\n");

        // Verify the header row
        assertTrue(csvLines[0].startsWith("Survey Reply ID"));

        // Verify the data rows
        assertEquals(surveyReplies.size(), csvLines.length - 1);

        // Add more assertions to verify the content of each row if needed
    }

}
