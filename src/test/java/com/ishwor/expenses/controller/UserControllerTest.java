package com.ishwor.expenses.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ishwor.expenses.dto.request.UserRequest;
import com.ishwor.expenses.dto.response.UserResponse;
import com.ishwor.expenses.exception.ResourceNotFoundException;
import com.ishwor.expenses.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserRequest userRequest;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        userRequest = new UserRequest(
                "John Doe",
                "john.doe@example.com",
                "password123"
        );

        userResponse = new UserResponse(
                1L,
                "John Doe",
                "john.doe@example.com"
        );
    }

    @Test
    void testRegisterUser() throws Exception {
        when(userService.registerUser(any(UserRequest.class))).thenReturn(userResponse);

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));

        verify(userService, times(1)).registerUser(any(UserRequest.class));
    }

    @Test
    void testGetUserById() throws Exception {
        when(userService.getUserById(1L)).thenReturn(userResponse);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));

        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    void testGetUserByEmail() throws Exception {
        when(userService.getUserByEmail("john.doe@example.com")).thenReturn(userResponse);

        mockMvc.perform(get("/api/users/email/john.doe@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));

        verify(userService, times(1)).getUserByEmail("john.doe@example.com");
    }

    @Test
    void testRegisterUser_WithInvalidName() throws Exception {
        UserRequest invalidRequest = new UserRequest(
                "A", // too short
                "john.doe@example.com",
                "password123"
        );

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).registerUser(any(UserRequest.class));
    }

    @Test
    void testRegisterUser_WithInvalidEmail() throws Exception {
        UserRequest invalidRequest = new UserRequest(
                "John Doe",
                "invalid-email", // invalid email format
                "password123"
        );

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).registerUser(any(UserRequest.class));
    }

    @Test
    void testRegisterUser_WithInvalidPassword() throws Exception {
        UserRequest invalidRequest = new UserRequest(
                "John Doe",
                "john.doe@example.com",
                "12345" // too short
        );

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).registerUser(any(UserRequest.class));
    }

    @Test
    void testGetUserById_NotFound() throws Exception {
        when(userService.getUserById(999L))
                .thenThrow(new ResourceNotFoundException("User not found"));

        mockMvc.perform(get("/api/users/999"))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUserById(999L);
    }

    @Test
    void testGetUserByEmail_NotFound() throws Exception {
        when(userService.getUserByEmail("nonexistent@example.com"))
                .thenThrow(new ResourceNotFoundException("User not found"));

        mockMvc.perform(get("/api/users/email/nonexistent@example.com"))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUserByEmail("nonexistent@example.com");
    }

    @Test
    void testRegisterUser_WithEmptyFields() throws Exception {
        UserRequest invalidRequest = new UserRequest("", "", "");

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).registerUser(any(UserRequest.class));
    }
}
