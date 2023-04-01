package com.example.surveyserver.controller;

import com.example.surveyserver.model.Survey;
import com.example.surveyserver.model.User;
import com.example.surveyserver.service.SurveyService;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class SurveyControllerTest {

    @InjectMocks
    private SurveyController surveyController;

    @Mock
    private SurveyService surveyService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(surveyController).build();
    }

    @Test
    public void testCreateSurvey() throws Exception {
        User user = new User();
        user.setId(1);
        Survey survey = new Survey();
        survey.setId(1);
        survey.setTitle("Test Survey");
        survey.setUser(user);

        when(surveyService.createSurvey(any(Survey.class), anyInt())).thenReturn(survey);

        mockMvc.perform(post("/api/surveys/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Test Survey\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Survey"));
    }

    @Test
    public void testGetSurvey() throws Exception {
        Survey survey = new Survey();
        survey.setId(1);
        survey.setTitle("Test Survey");

        when(surveyService.getSurvey(anyInt())).thenReturn(survey);

        mockMvc.perform(get("/api/surveys/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Survey"));
    }

    @Test
    public void testUpdateSurvey() throws Exception {
        Survey survey = new Survey();
        survey.setId(1);
        survey.setTitle("Test Survey");

        Survey updatedSurvey = new Survey();
        updatedSurvey.setId(1);
        updatedSurvey.setTitle("Updated Survey");

        when(surveyService.getSurvey(anyInt())).thenReturn(survey);
        when(surveyService.updateSurvey(any(Survey.class))).thenReturn(updatedSurvey);

        mockMvc.perform(put("/api/surveys/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Updated Survey\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
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
}

