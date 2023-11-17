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

    Optional<Survey> findByIdAndIsDeletedFalse(Integer id);

    Page<Survey> findByIsTemplateTrueAndIsDeletedFalseOrderByIdDesc(Pageable pageable);

    Page<Survey> findByIsTemplateFalseAndIsDeletedFalseOrderByIdDesc(Pageable pageable);

    @Query(value = "SELECT DISTINCT s.* FROM surveys s LEFT JOIN survey_access sar ON s.id = sar.survey_id WHERE (s.user_id = :userId OR sar.user_id = :userId) AND s.is_template = FALSE AND s.is_deleted = FALSE ORDER BY s.id DESC, s.created_at DESC",
            countQuery = "SELECT count(DISTINCT s.id) FROM surveys s LEFT JOIN survey_access sar ON s.id = sar.survey_id WHERE (s.user_id = :userId OR sar.user_id = :userId) AND s.is_template = FALSE AND s.is_deleted = FALSE",
            nativeQuery = true)
    Page<Survey> findSurveysByUser(@Param("userId") Integer userId, Pageable pageable);

}

