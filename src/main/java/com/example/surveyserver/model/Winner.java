package com.example.surveyserver.model;

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

    @Column(nullable = false)
    private Integer prizeId;

    @Column(nullable = false)
    private Integer surveyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Date wonAt;

    @PrePersist
    public void prePersist() {
        this.wonAt = new Date();
    }
}
