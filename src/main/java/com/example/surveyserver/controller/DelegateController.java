package com.example.surveyserver.controller;

import com.example.surveyserver.bean.DelegateRequest;
import com.example.surveyserver.model.Delegate;
import com.example.surveyserver.service.DelegateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/delegates")
public class DelegateController {

    @Autowired
    private DelegateService delegateService;

    @PostMapping
    public Delegate addDelegate(@RequestBody DelegateRequest delegateRequest) {
        return delegateService.addDelegate(delegateRequest.getDelegatorId(), delegateRequest.getDelegateId());
    }

    @DeleteMapping("/{id}")
    public void removeDelegate(@PathVariable Integer id) {
        delegateService.removeDelegate(id);
    }
}
