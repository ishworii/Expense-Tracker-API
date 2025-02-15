package com.ishwor.expenses.service;

import com.ishwor.expenses.dto.request.UserRequest;
import com.ishwor.expenses.dto.response.UserResponse;

public interface UserService {
    UserResponse registerUser(UserRequest request);
    UserResponse getUserById(Long id);
    UserResponse getUserByEmail(String email);
}
