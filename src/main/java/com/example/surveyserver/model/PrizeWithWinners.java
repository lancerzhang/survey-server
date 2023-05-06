package com.example.surveyserver.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PrizeWithWinners {
    private Prize prize;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<User> winners;

    public PrizeWithWinners(Prize prize, List<User> winners) {
        this.prize = prize;
        this.winners = winners;
    }
}
