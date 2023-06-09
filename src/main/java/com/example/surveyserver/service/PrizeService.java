package com.example.surveyserver.service;

import com.example.surveyserver.model.*;
import com.example.surveyserver.oauth2.PrincipalValidator;
import com.example.surveyserver.repository.PrizeRepository;
import com.example.surveyserver.repository.SurveyReplyRepository;
import com.example.surveyserver.repository.UserRepository;
import com.example.surveyserver.repository.WinnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PrizeService {

    @Autowired
    private PrizeRepository prizeRepository;

    @Autowired
    private WinnerRepository winnerRepository;

    @Autowired
    private SurveyReplyRepository surveyReplyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SurveyService surveyService;

    public List<User> createPrizeAndSelectWinners(Prize prize, Authentication authentication) {
        Integer surveyId = prize.getSurveyId();
        Survey survey = surveyService.getSurvey(surveyId);
        PrincipalValidator.validateUserPermission(survey.getUserId(), authentication);

        // Save the new prize
        prizeRepository.save(prize);

        // Get all survey replies and filter out the winners
        List<SurveyReply> surveyReplies = surveyReplyRepository.findBySurveyId(surveyId);
        List<Winner> winners = winnerRepository.findBySurveyId(surveyId);
        Set<Integer> winnerUserIds = winners.stream().map(w -> w.getUser().getId()).collect(Collectors.toSet());

        List<SurveyReply> eligibleReplies = surveyReplies.stream()
                .filter(reply -> !winnerUserIds.contains(reply.getUser().getId()))
                .collect(Collectors.toList());

        // Randomly select winners
        Collections.shuffle(eligibleReplies);
        List<SurveyReply> selectedReplies = eligibleReplies.subList(0, Math.min(prize.getQuantity(), eligibleReplies.size()));

        // Save the winners
        List<User> selectedWinners = new ArrayList<>();
        for (SurveyReply reply : selectedReplies) {
            Winner winner = new Winner();
            winner.setSurveyId(surveyId);
            User user = new User();
            user.setId(reply.getUser().getId());
            winner.setUser(user);
            winner.setPrize(prize);
            winnerRepository.save(winner);

            // Add the winner to the list
            selectedWinners.add(userRepository.findById(reply.getUser().getId()).get());
        }

        return selectedWinners;
    }

    public List<Prize> getPrizes(Integer surveyId) {
        return prizeRepository.findBySurveyId(surveyId);
    }

}
