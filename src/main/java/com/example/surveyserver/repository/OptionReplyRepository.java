package com.example.surveyserver.repository;

import com.example.surveyserver.model.OptionReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionReplyRepository extends JpaRepository<OptionReply, Integer> {
}