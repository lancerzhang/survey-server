package com.example.surveyserver.service;

import com.example.surveyserver.model.Delegate;
import com.example.surveyserver.model.User;
import com.example.surveyserver.repository.DelegateRepository;
import com.example.surveyserver.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DelegateServiceTest {

    @InjectMocks
    private DelegateService delegateService;

    @Mock
    private DelegateRepository delegateRepository;

    @Mock
    private UserRepository userRepository;

    private User delegator;
    private User delegate;

    @BeforeEach
    public void setUp() {
        delegator = new User();
        delegator.setId(1);

        delegate = new User();
        delegate.setId(2);
    }

    @Test
    public void testAddDelegate() {
        when(userRepository.findById(delegator.getId())).thenReturn(Optional.of(delegator));
        when(userRepository.findById(delegate.getId())).thenReturn(Optional.of(delegate));
        Delegate savedDelegate = new Delegate();
        savedDelegate.setId(1);
        savedDelegate.setDelegator(delegator);
        savedDelegate.setDelegate(delegate);
        when(delegateRepository.save(any(Delegate.class))).thenReturn(savedDelegate);

        Delegate result = delegateService.addDelegate(delegator.getId(), delegate.getId());

        assertEquals(savedDelegate, result);
        verify(userRepository, times(1)).findById(delegator.getId());
        verify(userRepository, times(1)).findById(delegate.getId());
        verify(delegateRepository, times(1)).save(any(Delegate.class));
    }

    @Test
    public void testAddDelegateInvalidDelegator() {
        when(userRepository.findById(delegator.getId())).thenReturn(Optional.empty());

        Delegate result = delegateService.addDelegate(delegator.getId(), delegate.getId());

        assertNull(result);
        verify(userRepository, times(1)).findById(delegator.getId());
    }

    @Test
    public void testAddDelegateInvalidDelegate() {
        when(userRepository.findById(delegator.getId())).thenReturn(Optional.of(delegator));
        when(userRepository.findById(delegate.getId())).thenReturn(Optional.empty());

        Delegate result = delegateService.addDelegate(delegator.getId(), delegate.getId());

        assertNull(result);
        verify(userRepository, times(1)).findById(delegator.getId());
        verify(userRepository, times(1)).findById(delegate.getId());
    }

    @Test
    public void testRemoveDelegate() {
        Integer delegateId = 1;
        delegateService.removeDelegate(delegateId);

        verify(delegateRepository, times(1)).deleteById(delegateId);
    }
}
