package com.example.surveyserver.service;

import com.example.surveyserver.model.Delegate;
import com.example.surveyserver.repository.DelegateRepository;
import com.example.surveyserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DelegateService {

    @Autowired
    private DelegateRepository delegateRepository;

    @Autowired
    private UserRepository userRepository;

    public Delegate addDelegate(Delegate delegate) {

        return delegateRepository.save(delegate);
    }

    public void removeDelegate(Integer id) {
        delegateRepository.deleteById(id);
    }
}
