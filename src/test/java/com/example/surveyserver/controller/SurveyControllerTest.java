package com.example.surveyserver.controller;

import com.example.surveyserver.model.QuestionSummary;
import com.example.surveyserver.model.Survey;
import com.example.surveyserver.model.SurveySummary;
import com.example.surveyserver.service.SurveyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class SurveyControllerTest {

    private final Integer userId = 1;
    private final Integer surveyId = 2;
    private final String surveyTitle = "Mock Survey";
    @InjectMocks
    private SurveyController surveyController;
    @Mock
    private SurveyService surveyService;
    private MockMvc mockMvc;
    private Survey mockSurvey;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(surveyController).build();
        mockSurvey = new Survey();
        mockSurvey.setUserId(userId);
        mockSurvey.setId(surveyId);
        mockSurvey.setTitle(surveyTitle);
    }

    @Test
    public void testCreateSurvey() throws Exception {

        when(surveyService.createSurvey(any(Survey.class))).thenReturn(mockSurvey);

        mockMvc.perform(post("/api/surveys")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Mock Survey\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(surveyId))
                .andExpect(jsonPath("$.title").value(surveyTitle));
    }

    @Test
    public void testGetSurvey() throws Exception {
        when(surveyService.getSurvey(anyInt())).thenReturn(mockSurvey);

        mockMvc.perform(get("/api/surveys/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(surveyId))
                .andExpect(jsonPath("$.title").value(surveyTitle));
    }

    @Test
    public void testUpdateSurvey() throws Exception {

        Survey updatedSurvey = new Survey();
        updatedSurvey.setId(surveyId);
        updatedSurvey.setTitle("Updated Survey");

        when(surveyService.getSurvey(anyInt())).thenReturn(mockSurvey);
        when(surveyService.updateSurvey(any(Survey.class))).thenReturn(updatedSurvey);

        mockMvc.perform(put("/api/surveys/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Updated Survey\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(surveyId))
                .andExpect(jsonPath("$.title").value("Updated Survey"));
    }

    @Test
    public void testGetSurveysByUser() throws Exception {
        Survey survey1 = new Survey();
        survey1.setId(1);
        survey1.setTitle("Test Survey 1");

        Survey survey2 = new Survey();
        survey2.setId(2);
        survey2.setTitle("Test Survey 2");

        Pageable pageable = PageRequest.of(0, 10);
        Page<Survey> surveyPage = new PageImpl<>(Arrays.asList(survey1, survey2), pageable, 2);

        when(surveyService.getSurveysByUser(anyInt(), any(Pageable.class))).thenReturn(surveyPage);

        mockMvc.perform(get("/api/surveys/user/1")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "createdAt")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].title").value("Test Survey 1"))
                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.content[1].title").value("Test Survey 2"));
    }

    @Test
    public void deleteSurvey_ShouldReturnOk_WhenSurveyIsDeleted() throws Exception {
        when(surveyService.deleteSurvey(surveyId)).thenReturn(mockSurvey);

        mockMvc.perform(delete("/api/surveys/{id}", surveyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(surveyId))
                .andExpect(jsonPath("$.title").value(surveyTitle));
    }

    @Test
    public void deleteSurvey_ShouldReturnNotFound_WhenSurveyIsNotDeleted() throws Exception {
        when(surveyService.deleteSurvey(surveyId)).thenReturn(null);

        mockMvc.perform(delete("/api/surveys/{id}", surveyId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void downloadRepliesAsCsv_ShouldReturnCsvFile_WhenCalledWithValidSurveyId() throws Exception {
        String csvContent = "Answer 1,Answer 2\nValue 1,Value 2";
        ByteArrayResource resource = new ByteArrayResource(csvContent.getBytes());

        when(surveyService.generateRepliesCsvContent(surveyId)).thenReturn(csvContent);

        mockMvc.perform(get("/api/surveys/{surveyId}/replies/csv", surveyId))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, "text/csv"))
                .andExpect(header().string(HttpHeaders.CONTENT_LENGTH, String.valueOf(csvContent.getBytes().length)))
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=survey_replies.csv"))
                .andExpect(content().bytes(resource.getByteArray()));
    }

    @Test
    public void getSurveySummary_ShouldReturnSurveySummary_WhenCalledWithValidSurveyId() throws Exception {
        int totalReplies = 10;

        Map<String, Integer> nameCounts = new HashMap<>();
        nameCounts.put("John", 8);
        Map<String, Double> namePercentages = new HashMap<>();
        namePercentages.put("John", 80.0);

        Map<String, Integer> colorCounts = new LinkedHashMap<>();
        colorCounts.put("Blue", 7);
        colorCounts.put("Green", 2);
        colorCounts.put("Red", 1);
        Map<String, Double> colorPercentages = new LinkedHashMap<>();
        colorPercentages.put("Blue", 70.0);
        colorPercentages.put("Green", 20.0);
        colorPercentages.put("Red", 10.0);

        List<QuestionSummary> questionSummaries = Arrays.asList(
                new QuestionSummary(1, "What is your name?", nameCounts, namePercentages),
                new QuestionSummary(2, "What is your favorite color?", colorCounts, colorPercentages)
        );
        SurveySummary expectedSummary = new SurveySummary(totalReplies, questionSummaries);

        when(surveyService.getSurveySummary(surveyId)).thenReturn(expectedSummary);

        mockMvc.perform(get("/api/surveys/{surveyId}/summary", surveyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalReplies").value(totalReplies))
                .andExpect(jsonPath("$.questionSummaries.[0].questionId").value(1))
                .andExpect(jsonPath("$.questionSummaries.[0].questionText").value("What is your name?"))
                .andExpect(jsonPath("$.questionSummaries.[0].optionCounts.John").value(8))
                .andExpect(jsonPath("$.questionSummaries.[0].optionPercentages.John").value(80.0))
                .andExpect(jsonPath("$.questionSummaries.[1].questionId").value(2))
                .andExpect(jsonPath("$.questionSummaries.[1].questionText").value("What is your favorite color?"))
                .andExpect(jsonPath("$.questionSummaries.[1].optionCounts.Blue").value(7))
                .andExpect(jsonPath("$.questionSummaries.[1].optionCounts.Green").value(2))
                .andExpect(jsonPath("$.questionSummaries.[1].optionCounts.Red").value(1))
                .andExpect(jsonPath("$.questionSummaries.[1].optionPercentages.Blue").value(70.0))
                .andExpect(jsonPath("$.questionSummaries.[1].optionPercentages.Green").value(20.0))
                .andExpect(jsonPath("$.questionSummaries.[1].optionPercentages.Red").value(10.0));
    }

}

