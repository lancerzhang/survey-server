package com.example.surveyserver.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
public class Subpage {
    @Id
    @GeneratedValue
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;
    private String title;
    private String description;
    @OneToMany(mappedBy = "subpage", cascade = CascadeType.PERSIST)
    private List<Question> questions;
}
