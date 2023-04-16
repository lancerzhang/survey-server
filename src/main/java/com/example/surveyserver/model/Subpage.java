package com.example.surveyserver.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "subpages")
public class Subpage {
    @Id
    @GeneratedValue
    private Integer id;

    private String title;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;
}
