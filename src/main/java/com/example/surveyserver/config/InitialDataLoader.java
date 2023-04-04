package com.example.surveyserver.config;

import com.example.surveyserver.model.Option;
import com.example.surveyserver.model.Question;
import com.example.surveyserver.model.Survey;
import com.example.surveyserver.model.User;
import com.example.surveyserver.service.SurveyService;
import com.example.surveyserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

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
        user.setStaffId("01234567");
        user.setEmail("sample.user@example.com");
        user = userService.createUser(user);

        // Create a sample survey
        Survey survey = new Survey();
        survey.setTitle("Sample Survey");
        survey.setDescription("A sample survey for testing purposes");
        survey.setAllowAnonymousReply(true);
        survey.setAllowResubmit(true);
        survey.setIsDeleted(false);
        survey.setUser(user);
        Question question1 = new Question();
        question1.setSeq(1);
        question1.setQuestionType("TEXT");
        question1.setQuestionText("Your name");
        Question question2 = new Question();
        question2.setSeq(2);
        question2.setQuestionType("RADIO");
        question2.setQuestionText("Your gender?");
        Option option21 = new Option();
        option21.setSeq(1);
        option21.setOptionText("Male");
        Option option22 = new Option();
        option22.setSeq(2);
        option22.setOptionText("Female");
        Option[] options2 = {option21, option22};
        question2.setOptions(Arrays.asList(options2));
        Question question3 = new Question();
        question3.setSeq(1);
        question3.setQuestionType("CHECKBOX");
        question3.setQuestionText("Your favor color?");
        Option option31 = new Option();
        option31.setSeq(1);
        option31.setOptionText("Red");
        Option option32 = new Option();
        option32.setSeq(2);
        option32.setOptionText("Blue");
        Option[] options3 = {option31, option32};
        question3.setOptions(Arrays.asList(options3));
        Question[] questions = {question1, question2, question3};
        survey.setQuestions(Arrays.asList(questions));
        surveyService.createSurvey(survey, user.getId());
    }
}
