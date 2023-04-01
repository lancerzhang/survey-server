package com.example.surveyserver.config;

import com.example.surveyserver.model.Survey;
import com.example.surveyserver.model.User;
import com.example.surveyserver.service.SurveyService;
import com.example.surveyserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitialDataLoader implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private SurveyService surveyService;

    @Override
    public void run(String... args) {
        // Create a sample user
        User user = new User();
        user.setUsername("sampleUser");
        user.setEmail("sample.user@example.com");
        user = userService.createUser(user);

        // Create a sample survey
        Survey survey = new Survey();
        survey.setTitle("Sample Survey");
        survey.setDescription("A sample survey for testing purposes");
        survey.setAllowAnonymousResponse(true);
        survey.setAllowResubmit(true);
        survey.setIsDeleted(false);
        survey.setUser(user);
        surveyService.createSurvey(survey,1);
    }
}
