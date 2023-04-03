package com.example.surveyserver.repository;

import com.example.surveyserver.model.Template;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Integer> {
    Page<Template> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
