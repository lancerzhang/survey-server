package com.example.surveyserver.service;

import com.example.surveyserver.model.Delegate;
import com.example.surveyserver.model.User;
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

    public Delegate addDelegate(Integer delegatorId, Integer delegateId) {
        User delegator = userRepository.findById(delegatorId).orElse(null);
        User delegate = userRepository.findById(delegateId).orElse(null);

        if (delegator == null || delegate == null) {
            return null;
        }

        Delegate newDelegate = new Delegate();
        newDelegate.setDelegator(delegator);
        newDelegate.setDelegate(delegate);

        return delegateRepository.save(newDelegate);
    }

    public void removeDelegate(Integer id) {
        delegateRepository.deleteById(id);
    }
}
