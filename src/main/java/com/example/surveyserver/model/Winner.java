package com.example.surveyserver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;


@Getter
@Setter
@Entity
@Table(name = "winners",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_winner_user_prize", columnNames = {"prize_id", "user_id"})
        },
        indexes = {
                @Index(name = "idx_winners_survey_id", columnList = "survey_id")
        })
public class Winner {
    @Id
    @GeneratedValue
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prize_id")
    @JsonIgnore
    private Prize prize;

    @Column(name = "survey_id", nullable = false)
    private Integer surveyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User user;

    private Date wonAt;

    @PrePersist
    public void prePersist() {
        this.wonAt = new Date();
    }
}
