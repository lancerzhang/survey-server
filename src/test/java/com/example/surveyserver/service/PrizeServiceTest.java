package com.example.surveyserver.service;

import com.example.surveyserver.model.Prize;
import com.example.surveyserver.model.Survey;
import com.example.surveyserver.model.SurveyReply;
import com.example.surveyserver.model.User;
import com.example.surveyserver.oauth2.PrincipalValidator;
import com.example.surveyserver.repository.PrizeRepository;
import com.example.surveyserver.repository.SurveyReplyRepository;
import com.example.surveyserver.repository.UserRepository;
import com.example.surveyserver.repository.WinnerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PrizeServiceTest {

    @InjectMocks
    private PrizeService prizeService;

    @Mock
    private SurveyService surveyService;

    @Mock
    private PrizeRepository prizeRepository;

    @Mock
    private WinnerRepository winnerRepository;

    @Mock
    private SurveyReplyRepository surveyReplyRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    public void testCreatePrizeAndSelectWinners() {


        Prize prize = new Prize();
        prize.setSurveyId(1);
        prize.setQuantity(2);

        User user1 = new User();
        user1.setId(1);
        SurveyReply surveyReply1 = new SurveyReply();
        surveyReply1.setUser(user1);

        User user2 = new User();
        user2.setId(2);
        SurveyReply surveyReply2 = new SurveyReply();
        surveyReply2.setUser(user2);

        User user3 = new User();
        user3.setId(3);
        SurveyReply surveyReply3 = new SurveyReply();
        surveyReply3.setUser(user3);

        List<SurveyReply> surveyReplies = Arrays.asList(surveyReply1, surveyReply2, surveyReply3);

        when(surveyReplyRepository.findBySurveyId(1)).thenReturn(surveyReplies);
        when(winnerRepository.findBySurveyId(1)).thenReturn(Collections.emptyList());
        Survey survey = new Survey();
        survey.setId(1);
        when(surveyService.getSurvey(1)).thenReturn(survey);
        // Updated stubbing
        when(userRepository.findById(anyInt())).thenAnswer(invocation -> {
            Integer userId = invocation.getArgument(0);
            User user = new User();
            user.setId(userId);
            return Optional.of(user);
        });

        try (MockedStatic<PrincipalValidator> mocked = Mockito.mockStatic(PrincipalValidator.class)) {
            mocked.when(() -> PrincipalValidator.validateUserPermission(anyInt(), any())).thenAnswer(i -> null);

            List<User> winners = prizeService.createPrizeAndSelectWinners(prize, null);
            assertEquals(2, winners.size());
        }
    }


    @Test
    public void testGetPrizes() {
        Prize prize1 = new Prize();
        prize1.setId(1);
        prize1.setSurveyId(1);
        Prize prize2 = new Prize();
        prize2.setId(2);
        prize2.setSurveyId(1);

        List<Prize> prizes = Arrays.asList(prize1, prize2);

        when(prizeRepository.findBySurveyId(1)).thenReturn(prizes);

        List<Prize> result = prizeService.getPrizes(1);

        assertEquals(2, result.size());
        assertEquals(prize1, result.get(0));
        assertEquals(prize2, result.get(1));
    }
}
