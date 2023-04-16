package com.example.surveyserver.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "winners")
public class Winner {
    @Id
    @GeneratedValue
    private int id;

    private Date wonAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prize_id", nullable = false)
    private Prize prize;

    @PrePersist
    public void prePersist() {
        this.wonAt = new Date();
    }
}
