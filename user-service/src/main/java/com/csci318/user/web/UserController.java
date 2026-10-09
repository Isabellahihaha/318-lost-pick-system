package com.csci318.user.web;

import com.csci318.user.domain.User;
import com.csci318.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody CreateUserRequest payload) {
        User created = service.register(payload);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.from(created));
    }

    @PostMapping("/login")
    public UserResponse login(@RequestBody LoginRequest payload) {
        return UserResponse.from(service.login(payload));
    }

    @GetMapping("/{username}")
    public UserResponse get(@PathVariable String username) {
        return UserResponse.from(service.get(username));
    }
}
