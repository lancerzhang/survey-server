package com.example.surveyserver.service;

import com.example.surveyserver.model.User;
import com.example.surveyserver.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private List<User> userList;

    @BeforeEach
    public void setUp() {
        User user1 = new User();
        user1.setId(1);
        user1.setDisplayName("john");
        user1.setEmployeeId("001");

        User user2 = new User();
        user2.setId(2);
        user2.setDisplayName("jane");
        user2.setEmployeeId("002");

        userList = Arrays.asList(user1, user2);
    }

    @Test
    public void testCreateUser() {
        User user = new User();
        user.setDisplayName("John");

        when(userRepository.save(user)).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertEquals(user.getDisplayName(), createdUser.getDisplayName());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testGetUser() {
        User user = new User();
        user.setId(1);
        user.setDisplayName("John");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        User foundUser = userService.getUser(1);

        assertEquals(user.getId(), foundUser.getId());
        assertEquals(user.getDisplayName(), foundUser.getDisplayName());
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    public void testGetUserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        User foundUser = userService.getUser(1);

        assertNull(foundUser);
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    public void testGetAllUsers() {
        User user1 = new User();
        user1.setDisplayName("John");

        User user2 = new User();
        user2.setDisplayName("Jane");

        List<User> users = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(users);

        List<User> foundUsers = userService.getAllUsers();

        assertEquals(2, foundUsers.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testUpdateUser() {
        User existingUser = new User();
        existingUser.setId(1);
        existingUser.setDisplayName("John");

        User updatedUser = new User();
        updatedUser.setDisplayName("Jane");

        when(userRepository.findById(1)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);

        User result = userService.updateUser(1, updatedUser);

        assertEquals(updatedUser.getDisplayName(), result.getDisplayName());
        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).save(updatedUser);
    }

    @Test
    public void testUpdateUserNotFound() {
        User updatedUser = new User();
        updatedUser.setDisplayName("Jane");

        when(userRepository.findById(1)).thenReturn(Optional.empty());

        User result = userService.updateUser(1, updatedUser);

        assertNull(result);
        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    public void testSearchUsers() {
        String searchString = "john";
        Pageable pageable = PageRequest.of(0, 10);
        when(userRepository.searchByDisplayNameOrEmployeeId(searchString, pageable)).thenReturn(userList);

        List<User> result = userService.searchUsers(searchString);

        assertEquals(userList, result);
        verify(userRepository, times(1)).searchByDisplayNameOrEmployeeId(searchString, pageable);
    }
}

