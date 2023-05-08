package com.example.surveyserver.controller;

import com.example.surveyserver.model.Prize;
import com.example.surveyserver.model.User;
import com.example.surveyserver.service.PrizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/prizes")
public class PrizeController {

    @Autowired
    private PrizeService prizeService;

    @PostMapping
    public List<User> createPrizeAndSelectWinners(@Valid @RequestBody Prize prize) {
        return prizeService.createPrizeAndSelectWinners(prize);
    }

    // get prizes with winners by surveyId
    @GetMapping
    public List<Prize> getPrizes(@Param("surveyId") Integer surveyId) {
        return prizeService.getPrizes(surveyId);
    }
}
