package com.example.surveyserver.config;

import com.example.surveyserver.model.Question;
import com.example.surveyserver.model.Survey;
import com.example.surveyserver.model.SurveyReply;
import com.example.surveyserver.model.User;
import com.example.surveyserver.repository.QuestionRepository;
import com.example.surveyserver.service.SurveyReplyService;
import com.example.surveyserver.service.SurveyService;
import com.example.surveyserver.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

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
                "    \"title\": \"Test Survey 1\",\n" +
                "    \"description\": \"Tes Survey Description 1\",\n" +
                "    \"questions\":[\n" +
                "      {\n" +
                "          \"seq\": 1,\n" +
                "          \"questionType\": \"TEXT\",\n" +
                "          \"questionText\": \"Your name?\"\n" +
                "      },\n" +
                "      {\n" +
                "          \"seq\": 2,\n" +
                "          \"questionType\": \"RADIO\",\n" +
                "          \"questionText\": \"Your Gender?\",\n" +
                "          \"options\": [\n" +
                "              {\n" +
                "                  \"optionText\": \"Male\",\n" +
                "                  \"seq\": 1\n" +
                "              },\n" +
                "              {\n" +
                "                  \"optionText\": \"Female\",\n" +
                "                  \"seq\": 2\n" +
                "              }\n" +
                "          ]\n" +
                "      },\n" +
                "      {\n" +
                "          \"seq\": 3,\n" +
                "          \"questionType\": \"CHECKBOX\",\n" +
                "          \"questionText\": \"Your favor color?\",\n" +
                "          \"options\": [\n" +
                "              {\n" +
                "                  \"optionText\": \"red\",\n" +
                "                  \"seq\": 1\n" +
                "              },\n" +
                "              {\n" +
                "                  \"optionText\": \"blue\",\n" +
                "                  \"seq\": 2\n" +
                "              },\n" +
                "              {\n" +
                "                  \"optionText\": \"green\",\n" +
                "                  \"seq\": 3\n" +
                "              }\n" +
                "          ]\n" +
                "      }\n" +
                "  ]\n" +
                "  }";
        Survey survey = new ObjectMapper().readValue(surveyJsonStr, Survey.class);
        surveyService.createSurvey(survey, user.getId());

        List<Question> allQuestions = questionRepository.findAll();
        Survey survey1 = surveyService.getSurvey(survey.getId());

        String replyJsonStr = "{\n" +
                "    \"user\": {\n" +
                "        \"id\": 1\n" +
                "    },\n" +
                "    \"survey\": {\n" +
                "        \"id\": 2\n" +
                "    },\n" +
                "    \"questionReplies\": [\n" +
                "        {\n" +
                "            \"question\": {\n" +
                "                \"id\": 3\n" +
                "            },\n" +
                "            \"replyText\": \"Bill\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"question\": {\n" +
                "                \"id\": 4\n" +
                "            },\n" +
                "            \"optionReplies\": [\n" +
                "                {\n" +
                "                    \"option\": {\n" +
                "                        \"id\": 5\n" +
                "                    },\n" +
                "                    \"selected\": true\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"question\": {\n" +
                "                \"id\": 7\n" +
                "            },\n" +
                "            \"optionReplies\": [\n" +
                "                {\n" +
                "                    \"option\": {\n" +
                "                        \"id\": 8\n" +
                "                    },\n" +
                "                    \"selected\": true\n" +
                "                }\n" +
                "            ]\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        SurveyReply surveyReply = new ObjectMapper().readValue(replyJsonStr, SurveyReply.class);
        surveyReplyService.createSurveyReply(surveyReply);
    }
}
