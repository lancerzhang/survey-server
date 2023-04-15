package com.example.surveyserver.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Delegate {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private Integer delegator_id;

    @Column(nullable = false)
    private Integer delegate_id;
}
