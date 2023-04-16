package com.example.surveyserver.repository;

import com.example.surveyserver.model.Delegate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DelegateRepository extends JpaRepository<Delegate, Integer> {
}
