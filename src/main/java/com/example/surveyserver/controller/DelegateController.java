package com.example.surveyserver.controller;

import com.example.surveyserver.model.Delegate;
import com.example.surveyserver.service.DelegateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/delegates")
public class DelegateController {

    @Autowired
    private DelegateService delegateService;

    @PostMapping
    public Delegate addDelegate(@Valid @RequestBody Delegate delegate) {
        return delegateService.addDelegate(delegate);
    }

    @DeleteMapping("/{id}")
    public void removeDelegate(@PathVariable Integer id) {
        delegateService.removeDelegate(id);
    }

    @GetMapping("/delegator/{delegatorId}")
    public List<Delegate> getDelegatesByDelegatorId(@PathVariable Integer delegatorId) {
        return delegateService.getDelegatesByDelegatorId(delegatorId);
    }
}
