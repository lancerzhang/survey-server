package com.example.surveyserver.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "delegate")
@Getter
@Setter
public class Delegate {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delegator_id", nullable = false)
    private User delegator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delegate_id", nullable = false)
    private User delegate;
}
