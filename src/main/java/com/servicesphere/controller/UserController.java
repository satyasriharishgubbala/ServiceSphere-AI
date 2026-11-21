package com.servicesphere.controller;

import com.servicesphere.dto.UserRequestDTO;
import com.servicesphere.dto.UserResponseDTO;
import com.servicesphere.entity.User;
import com.servicesphere.service.UserService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET - Retrieve all users
    @GetMapping
    public List<UserResponseDTO> getAllUsers() {

        return userService.getAllUsers()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    // GET - Retrieve user by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {

        return userService.getUserById(id)
                .map(user -> ResponseEntity.ok(convertToDTO(user)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST - Register a new user
    @PostMapping
    public ResponseEntity<UserResponseDTO> registerUser(
            @Valid @RequestBody UserRequestDTO requestDTO) {

        try {
            User savedUser = userService.registerUser(requestDTO);

            UserResponseDTO responseDTO = convertToDTO(savedUser);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(responseDTO);

        } catch (IllegalArgumentException exception) {

            return ResponseEntity.badRequest().build();
        }
    }

    // Convert Entity to DTO
    private UserResponseDTO convertToDTO(User user) {

        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }
}