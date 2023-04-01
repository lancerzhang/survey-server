package com.example.surveyserver.repository;

import com.example.surveyserver.model.Delegate;
import com.example.surveyserver.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DelegateRepository  extends JpaRepository<Delegate, Integer> {
}
