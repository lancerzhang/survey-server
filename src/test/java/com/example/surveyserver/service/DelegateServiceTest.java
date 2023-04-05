package com.example.surveyserver.service;
import com.example.surveyserver.model.Delegate;
import com.example.surveyserver.repository.DelegateRepository;
import com.example.surveyserver.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
}
