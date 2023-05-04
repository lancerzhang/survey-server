package com.example.surveyserver.config;

import com.example.surveyserver.model.Survey;
import com.example.surveyserver.model.SurveyReply;
import com.example.surveyserver.model.User;
import com.example.surveyserver.repository.OptionReplyRepository;
import com.example.surveyserver.repository.QuestionReplyRepository;
import com.example.surveyserver.repository.QuestionRepository;
import com.example.surveyserver.repository.SurveyReplyRepository;
import com.example.surveyserver.service.SurveyReplyService;
import com.example.surveyserver.service.SurveyService;
import com.example.surveyserver.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitialDataLoader implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private SurveyService surveyService;

    @Autowired
    private SurveyReplyService surveyReplyService;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private SurveyReplyRepository surveyReplyRepository;

    @Autowired
    private QuestionReplyRepository questionReplyRepository;

    @Autowired
    private OptionReplyRepository optionReplyRepository;


    @Override
    public void run(String... args) throws JsonProcessingException {
        // Create a sample user
        String userJsonStr = "{\n" +
                "  \"username\": \"sampleUser\",\n" +
                "  \"staffId\": \"01234567\",\n" +
                "  \"email\": \"sample.user@example.com\"\n" +
                "}";
        User user = new ObjectMapper().readValue(userJsonStr, User.class);
        user = userService.createUser(user);

        // Create a sample survey
        String surveyJsonStr = "{\n" +
                "    \"userId\": 1,\n" +
                "    \"title\": \"Test Survey 1\",\n" +
                "    \"description\": \"Tes Survey Description 1\",\n" +
                "    \"isTemplate\": false,\n" +
                "    \"questions\":[\n" +
                "      {\n" +
                "          \"questionType\": \"TEXT\",\n" +
                "          \"questionText\": \"Your name?\"\n" +
                "      },\n" +
                "      {\n" +
                "          \"questionType\": \"RADIO\",\n" +
                "          \"questionText\": \"Your Gender?\",\n" +
                "          \"options\": [\n" +
                "              {\n" +
                "                  \"optionText\": \"Male\"\n" +
                "              },\n" +
                "              {\n" +
                "                  \"optionText\": \"Female\"\n" +
                "              }\n" +
                "          ]\n" +
                "      },\n" +
                "      {\n" +
                "          \"questionType\": \"CHECKBOX\",\n" +
                "          \"questionText\": \"Your favor color?\",\n" +
                "          \"options\": [\n" +
                "              {\n" +
                "                  \"optionText\": \"red\"\n" +
                "              },\n" +
                "              {\n" +
                "                  \"optionText\": \"blue\"\n" +
                "              },\n" +
                "              {\n" +
                "                  \"optionText\": \"green\"\n" +
                "              }\n" +
                "          ]\n" +
                "      }\n" +
                "  ]\n" +
                "  }";
        Survey survey = new ObjectMapper().readValue(surveyJsonStr, Survey.class);
        surveyService.createSurvey(survey);

//        List<Question> allQuestions = questionRepository.findAll();
//        Survey survey1 = surveyService.getSurvey(survey.getId());

        String replyJsonStr = "{\n" +
                "    \"userId\": 1,\n" +
                "    \"surveyId\": 2,\n" +
                "    \"questionReplies\": [\n" +
                "        {\n" +
                "            \"questionId\": 3,\n" +
                "            \"replyText\": \"Bill\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"questionId\": 4,\n" +
                "            \"optionReplies\": [\n" +
                "                {\n" +
                "                    \"optionId\": 5,\n" +
                "                    \"selected\": true\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"questionId\": 7,\n" +
                "            \"optionReplies\": [\n" +
                "                {\n" +
                "                    \"optionId\": 8,\n" +
                "                    \"selected\": true\n" +
                "                }\n" +
                "            ]\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        SurveyReply surveyReply = new ObjectMapper().readValue(replyJsonStr, SurveyReply.class);
        surveyReplyService.createSurveyReply(surveyReply);

//        List<SurveyReply> allReplies = surveyReplyRepository.findAll();
//        List<QuestionReply> allQuestionReply = questionReplyRepository.findAll();
//        List<OptionReply> allOptionReplies = optionReplyRepository.findAll();
//        System.out.println(allReplies);
    }
}
