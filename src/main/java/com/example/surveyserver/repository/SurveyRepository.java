package com.example.surveyserver.repository;

import com.example.surveyserver.model.Survey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, Integer> {
    Page<Survey> findByUserIdAndIsTemplateFalseAndIsDeletedFalseOrderByIdDesc(Integer userId, Pageable pageable);

    Optional<Survey> findByIdAndIsDeletedFalse(Integer id);
    Page<Survey> findByIsTemplateTrueOrderByIdDesc(Pageable pageable);
}

