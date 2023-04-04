package com.example.surveyserver.repository;

import com.example.surveyserver.model.SurveyReply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyReplyRepository extends JpaRepository<SurveyReply, Integer> {
    Page<SurveyReply> findByUserIdOrderByCreatedAtDesc(Integer userId, Pageable pageable);

    List<SurveyReply> findBySurveyId(Integer surveyId);
}
