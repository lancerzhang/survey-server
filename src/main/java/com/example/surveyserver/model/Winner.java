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
@Table(name = "winners")
public class Winner {
    @Id
    @GeneratedValue
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prize_id")
    @JsonIgnore
    private Prize prize;

    @Column(nullable = false)
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
