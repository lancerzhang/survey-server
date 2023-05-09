package com.example.surveyserver.service;

import com.example.surveyserver.model.Delegate;
import com.example.surveyserver.model.User;
import com.example.surveyserver.oauth2.PrincipalUser;
import com.example.surveyserver.oauth2.PrincipalValidator;
import com.example.surveyserver.repository.DelegateRepository;
import com.example.surveyserver.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    private PrincipalUser dummyPrincipal;

    @BeforeEach
    public void setUp() {
        Delegate delegate1 = new Delegate();
        delegate1.setId(1);
        // Set other properties for delegate1

        Delegate delegate2 = new Delegate();
        delegate2.setId(2);
        // Set other properties for delegate2

        delegateList = Arrays.asList(delegate1, delegate2);

        dummyPrincipal = new PrincipalUser(1, "01234567", "sampleUser", "sample.user@example.com");
        dummyPrincipal.setDelegators(Collections.singletonList(1));
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
    public void testGetDelegatesByDelegatorId() {
        Integer delegatorId = 1;
        when(delegateRepository.findByDelegatorId(delegatorId)).thenReturn(delegateList);

        List<Delegate> result = delegateService.getDelegatesByDelegatorId(delegatorId);

        assertEquals(delegateList, result);
        verify(delegateRepository, times(1)).findByDelegatorId(delegatorId);
    }

    @Test
    public void removeDelegateSuccessfully() {
        // Arrange
        Integer id = 1;
        Delegate delegate = new Delegate();
        User delegator = new User();
        delegator.setId(1);
        delegate.setDelegator(delegator);

        when(delegateRepository.findById(id)).thenReturn(Optional.of(delegate));
        try (MockedStatic<PrincipalValidator> mocked = Mockito.mockStatic(PrincipalValidator.class)) {
            mocked.when(() -> PrincipalValidator.validateUserPermission(anyInt(), any())).thenAnswer(i -> null);

            // Act
            delegateService.removeDelegate(id, null);

            // Assert
            verify(delegateRepository, times(1)).findById(id);
            verify(delegateRepository, times(1)).deleteById(id);
            mocked.verify(() -> PrincipalValidator.validateUserPermission(anyInt(), any()), times(1));
        }
    }

}
