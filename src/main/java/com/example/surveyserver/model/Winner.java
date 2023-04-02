package com.example.surveyserver.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "winners")
public class Winner {
    @Id
    @GeneratedValue
    private int id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;
    
    @ManyToOne
    @JoinColumn(name = "prize_id", nullable = false)
    private Prize prize;

    @Column(nullable = false, updatable = false)
    private Date wonAt;

    @PrePersist
    public void prePersist() {
        this.wonAt = new Date();
    }
}
