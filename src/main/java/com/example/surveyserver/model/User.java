package com.example.surveyserver.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;
    private String password;
    @Column(nullable = false)
    private boolean anonymous;
    private String publicKey;

    private String email;
    @OneToMany(mappedBy = "delegator", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Delegate> delegates;
    private Timestamp createdAt;
    private Timestamp lastModified;
}
