package com.example.surveyserver.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name="subpages")
public class Subpage {
    @Id
    @GeneratedValue
    private Integer id;

    private String title;

    private String description;

    @OneToMany(mappedBy = "subpage", fetch = FetchType.LAZY)
    private List<Question> questions;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;
}
