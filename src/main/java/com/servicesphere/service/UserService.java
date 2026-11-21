package com.servicesphere.service;

import com.servicesphere.entity.User;
import com.servicesphere.enums.Role;
import com.servicesphere.repository.UserRepository;
import com.servicesphere.dto.UserRequestDTO;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                      PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get user by ID
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Get user by email
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Check whether email already exists
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    // Register new user
    public User registerUser(UserRequestDTO requestDTO) {

        // Check for duplicate email
        if (userRepository.existsByEmail(requestDTO.getEmail())) {
            throw new IllegalArgumentException("Email is already registered");
        }

        // Create user entity
        User user = new User();

        user.setName(requestDTO.getName());
        user.setEmail(requestDTO.getEmail());

        // Hash password using BCrypt
        user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));

        // Assign default role
        user.setRole(Role.EMPLOYEE);

        // Save user in database
        return userRepository.save(user);
    }
}