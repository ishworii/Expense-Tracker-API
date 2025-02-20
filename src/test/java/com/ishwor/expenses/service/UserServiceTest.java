package com.ishwor.expenses.service;

import com.ishwor.expenses.dto.request.UserRequest;
import com.ishwor.expenses.dto.response.UserResponse;
import com.ishwor.expenses.exception.ResourceNotFoundException;
import com.ishwor.expenses.model.User;
import com.ishwor.expenses.repository.UserRepository;
import com.ishwor.expenses.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private UserRequest userRequest;

    @BeforeEach
    void setUp() {
        
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("John Doe");
        testUser.setEmail("john.doe@example.com");
        testUser.setPassword("securepassword123");

        
        userRequest = new UserRequest("John Doe", "john.doe@example.com", "securepassword123");
    }

    @Test
    void testRegisterUser() {
        
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        
        UserResponse response = userService.registerUser(userRequest);

        
        assertNotNull(response);
        assertEquals("John Doe", response.name());
        assertEquals("john.doe@example.com", response.email());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testGetUserById_Success() {
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        
        UserResponse response = userService.getUserById(1L);

        
        assertNotNull(response);
        assertEquals("John Doe", response.name());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testGetUserById_NotFound() {
        
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(99L));
        verify(userRepository, times(1)).findById(99L);
    }

    @Test
    void testGetUserByEmail_Success() {
        
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(testUser));

        
        UserResponse response = userService.getUserByEmail("john.doe@example.com");

        
        assertNotNull(response);
        assertEquals("John Doe", response.name());
        verify(userRepository, times(1)).findByEmail("john.doe@example.com");
    }

    @Test
    void testGetUserByEmail_NotFound() {
        
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserByEmail("unknown@example.com"));
        verify(userRepository, times(1)).findByEmail("unknown@example.com");
    }
}
