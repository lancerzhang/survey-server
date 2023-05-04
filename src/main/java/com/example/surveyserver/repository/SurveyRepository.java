package com.example.surveyserver.repository;

import com.example.surveyserver.model.Survey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, Integer> {
    Page<Survey> findByUserIdAndIsTemplateFalseAndIsDeletedFalseOrderByIdDesc(Integer userId, Pageable pageable);

    Optional<Survey> findByIdAndIsDeletedFalse(Integer id);

    Page<Survey> findByIsTemplateTrueAndIsDeletedFalseOrderByIdDesc(Pageable pageable);

    Page<Survey> findByIsTemplateFalseAndIsDeletedFalseOrderByIdDesc(Pageable pageable);

    @Query(value = "SELECT DISTINCT s.* FROM surveys s JOIN survey_replies sr ON s.id = sr.survey_id WHERE sr.user_id = :userId",
            countQuery = "SELECT COUNT(DISTINCT s.id) FROM surveys s JOIN survey_reply sr ON s.id = sr.survey_id WHERE sr.user_id = :userId",
            nativeQuery = true)
    Page<Survey> findRepliedSurveysByUserId(@Param("userId") Integer userId, Pageable pageable);
}

