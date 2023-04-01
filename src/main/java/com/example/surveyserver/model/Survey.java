package com.example.surveyserver.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
public class Survey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private String title;
    private String description;
    private Boolean allowAnonymousReply;
    private Boolean allowResubmit;
    private Boolean isDeleted;
    private Timestamp lastModified;
    private Timestamp startTime;
    private Timestamp endTime;
    private Integer maxReplies;
    private Timestamp createdAt;
}
