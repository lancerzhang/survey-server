package com.example.surveyserver.controller;

import com.example.surveyserver.model.Delegate;
import com.example.surveyserver.oauth2.PrincipalValidator;
import com.example.surveyserver.service.DelegateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/delegates")
public class DelegateController {

    @Autowired
    private DelegateService delegateService;

    @PostMapping
    public Delegate addDelegate(@Valid @RequestBody Delegate delegate, Authentication authentication) {
        PrincipalValidator.validateUserPermission(delegate.getDelegator().getId(), authentication);
        return delegateService.addDelegate(delegate);
    }

    @DeleteMapping("/{id}")
    public void removeDelegate(@PathVariable Integer id, Authentication authentication) {
        delegateService.removeDelegate(id, authentication);
    }

    @GetMapping("/delegator/{delegatorId}")
    public List<Delegate> getDelegatesByDelegatorId(@PathVariable Integer delegatorId) {
        return delegateService.getDelegatesByDelegatorId(delegatorId);
    }

    @GetMapping("/delegate/{delegateId}")
    public List<Delegate> getDelegatorsByDelegateId(@PathVariable Integer delegateId) {
        return delegateService.getDelegatorsByDelegateId(delegateId);
    }
}
