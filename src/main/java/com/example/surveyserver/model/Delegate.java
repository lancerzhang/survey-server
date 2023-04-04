package com.example.surveyserver.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Delegate {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "delegator_id", nullable = false)
    private User delegator;

    @ManyToOne
    @JoinColumn(name = "delegate_id", nullable = false)
    private User delegate;
}
