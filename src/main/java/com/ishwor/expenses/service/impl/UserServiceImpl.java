package com.ishwor.expenses.service.impl;

import com.ishwor.expenses.dto.request.UserRequest;
import com.ishwor.expenses.dto.response.UserResponse;
import com.ishwor.expenses.exception.ResourceNotFoundException;
import com.ishwor.expenses.model.User;
import com.ishwor.expenses.repository.UserRepository;
import com.ishwor.expenses.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponse registerUser(UserRequest request) {
        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(request.password()); // 🚨 TODO: Hash password before saving (security best practice)

        User savedUser = userRepository.save(user);

        return new UserResponse(savedUser.getId(), savedUser.getName(), savedUser.getEmail());
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }

    @Override
    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }
}
