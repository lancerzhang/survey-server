package com.example.surveyserver.service;

import com.example.surveyserver.model.Delegate;
import com.example.surveyserver.repository.DelegateRepository;
import com.example.surveyserver.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DelegateServiceTest {

    @InjectMocks
    private DelegateService delegateService;

    @Mock
    private DelegateRepository delegateRepository;

    @Mock
    private UserRepository userRepository;

    private List<Delegate> delegateList;

    @BeforeEach
    public void setUp() {
        Delegate delegate1 = new Delegate();
        delegate1.setId(1);
        // Set other properties for delegate1

        Delegate delegate2 = new Delegate();
        delegate2.setId(2);
        // Set other properties for delegate2

        delegateList = Arrays.asList(delegate1, delegate2);
    }

    @Test
    public void testAddDelegate() {
        Delegate delegate = new Delegate();
        delegate.setId(1);

        when(delegateRepository.save(delegate)).thenReturn(delegate);

        Delegate createdDelegate = delegateService.addDelegate(delegate);

        assertEquals(delegate.getId(), createdDelegate.getId());
        verify(delegateRepository, times(1)).save(delegate);
    }

    @Test
    public void testRemoveDelegate() {
        Integer delegateId = 1;

        delegateService.removeDelegate(delegateId);

        verify(delegateRepository, times(1)).deleteById(delegateId);
    }

    @Test
    public void testGetDelegatesByDelegatorId() {
        Integer delegatorId = 1;
        when(delegateRepository.findByDelegatorId(delegatorId)).thenReturn(delegateList);

        List<Delegate> result = delegateService.getDelegatesByDelegatorId(delegatorId);

        assertEquals(delegateList, result);
        verify(delegateRepository, times(1)).findByDelegatorId(delegatorId);
    }
}
