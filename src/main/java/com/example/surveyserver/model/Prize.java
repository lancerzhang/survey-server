package com.example.surveyserver.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
public class Prize {
    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;
    private String name;
    private String description;
    private Integer quantity;
    private Timestamp createdAt;
    private Timestamp lastModified;
}
