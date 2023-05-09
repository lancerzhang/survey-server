package com.example.surveyserver.service;

import com.example.surveyserver.exception.ResourceNotFoundException;
import com.example.surveyserver.model.Delegate;
import com.example.surveyserver.oauth2.PrincipalValidator;
import com.example.surveyserver.repository.DelegateRepository;
import com.example.surveyserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DelegateService {

    @Autowired
    private DelegateRepository delegateRepository;

    @Autowired
    private UserRepository userRepository;

    public Delegate addDelegate(Delegate delegate) {
        return delegateRepository.save(delegate);
    }

    public void removeDelegate(Integer id, Authentication authentication) {
        // Fetch the delegate from the repository
        Delegate delegate = delegateRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Delegate not found with ID: " + id));

        // Validate if the authenticated user has the right to remove the delegate
        PrincipalValidator.validateUserPermission(delegate.getDelegator().getId(), authentication);

        delegateRepository.deleteById(id);
    }

    public List<Delegate> getDelegatesByDelegatorId(Integer delegatorId) {
        return delegateRepository.findByDelegatorId(delegatorId);
    }

    public List<Delegate> getDelegatorsByDelegateId(Integer delegateId) {
        return delegateRepository.findByDelegateId(delegateId);
    }
}
