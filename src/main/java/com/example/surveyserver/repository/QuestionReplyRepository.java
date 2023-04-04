package com.example.surveyserver.repository;

import com.example.surveyserver.model.QuestionReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionReplyRepository extends JpaRepository<QuestionReply, Integer> {
}
