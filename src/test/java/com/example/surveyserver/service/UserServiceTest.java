package com.example.surveyserver.service;

import com.example.surveyserver.model.User;
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
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1);
    }

    @Test
    public void testCreateUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.createUser(user);

        assertEquals(user, result);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testGetUser() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));

        User result = userService.getUser(1);

        assertEquals(user, result);
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    public void testUpdateUser() {
        User updatedUser = new User();
        updatedUser.setId(1);
        updatedUser.setUsername("updated");

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        User result = userService.updateUser(1, updatedUser);

        assertEquals(updatedUser, result);
        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).save(updatedUser);
    }

    @Test
    public void testUpdateUserNotFound() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        User result = userService.updateUser(1, user);

        assertNull(result);
        verify(userRepository, times(1)).findById(1);
    }
}
