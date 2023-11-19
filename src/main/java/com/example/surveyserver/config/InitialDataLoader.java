package com.example.surveyserver.config;

import com.example.surveyserver.model.*;
import com.example.surveyserver.repository.SurveyReplyRepository;
import com.example.surveyserver.service.*;
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
    private DelegateService delegateService;

    @Autowired
    private SurveyReplyRepository surveyReplyRepository;
    @Autowired
    private SurveyAccessService surveyAccessService;

    @Override
    public void run(String... args) throws JsonProcessingException {
        // Create a sample user
        String userJsonStr = "{\n" +
                "  \"displayName\": \"sampleUser\",\n" +
                "  \"employeeId\": \"01234567\",\n" +
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
                "    \"allowResubmit\": false,\n" +
                "    \"isAnonymous\": false,\n" +
                "    \"questions\":[\n" +
                "      {\n" +
                "          \"questionType\": \"TEXT\",\n" +
                "          \"questionText\": \"Your name?\"\n" +
                "      },\n" +
                "      {\n" +
                "          \"questionType\": \"CHOICE\",\n" +
                "          \"maxSelection\": 1,\n" +
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
                "          \"questionType\": \"CHOICE\",\n" +
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
                "    \"user\": {\n" +
                "        \"id\": 1\n" +
                "    },\n" +
                "    \"survey\": {\n" +
                "        \"id\": 2\n" +
                "    },\n" +
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
                "                },\n" +
                "                {\n" +
                "                    \"optionId\": 6,\n" +
                "                    \"selected\": false\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"questionId\": 7,\n" +
                "            \"optionReplies\": [\n" +
                "                {\n" +
                "                    \"optionId\": 8,\n" +
                "                    \"selected\": true\n" +
                "                },\n" +
                "                {\n" +
                "                    \"optionId\": 9,\n" +
                "                    \"selected\": false\n" +
                "                },\n" +
                "                {\n" +
                "                    \"optionId\": 10,\n" +
                "                    \"selected\": false\n" +
                "                }\n" +
                "            ]\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        SurveyReply surveyReply = new ObjectMapper().readValue(replyJsonStr, SurveyReply.class);
        surveyReplyService.createSurveyReply(surveyReply, survey);

//        List<SurveyReply> allReplies = surveyReplyRepository.findAll();
//        List<QuestionReply> allQuestionReply = questionReplyRepository.findAll();
//        List<OptionReply> allOptionReplies = optionReplyRepository.findAll();
//        System.out.println(allReplies);

        String userJsonStr2 = "{\n" +
                "  \"displayName\": \"Bill Gates\",\n" +
                "  \"employeeId\": \"43215678\",\n" +
                "  \"email\": \"bill.gates@example.com\"\n" +
                "}";
        User user2 = new ObjectMapper().readValue(userJsonStr2, User.class);
        userService.createUser(user2);

        String delegateJsonStr = "{\n" +
                "    \"delegator\": {\n" +
                "        \"id\": " + user2.getId() + "\n" +
                "    },\n" +
                "    \"delegate\": {\n" +
                "        \"id\": 1\n" +
                "    }\n" +
                "}";
        Delegate delegate = new ObjectMapper().readValue(delegateJsonStr, Delegate.class);
        delegateService.addDelegate(delegate);

        String surveyAccessJsonStr = "{\n" +
                "    \"surveyId\": "+survey.getId()+",\n" +
                "    \"user\": {\n" +
                "        \"id\": " + user2.getId() + "\n" +
                "    }\n" +
                "}";
        SurveyAccess surveyAccess = new ObjectMapper().readValue(surveyAccessJsonStr, SurveyAccess.class);
        surveyAccessService.addSurveyAccess(surveyAccess);
    }
}
