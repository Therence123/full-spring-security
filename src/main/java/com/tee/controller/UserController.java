package com.tee.controller;

import java.util.List;

import javax.persistence.EntityExistsException;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tee.model.User;
import com.tee.repository.UserRepository;


@RestController
@RequestMapping(path="user")
public class UserController {


    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // example A: without authentication
    @GetMapping()
    public String hello() {
        return "Hello world!";
    }

    // example B: with boolean statement
    @PreAuthorize("isAnonymous() && #text.length() < 6")
    @GetMapping("/message/{text}")
    public String hello(@PathVariable String text) {
        return "Hello anonymus user!";
    }

    // example C: endpoint only for authenticated users
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // example D: endpoint for users with role ADMIN
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public User createUser(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // example E: endpoint for authenticated users which ask about his own User
    @PreAuthorize("isAuthenticated() && @securityAuthorizationService.isCurrentUser(#username)")
    @GetMapping("/{username}")
    public User getUser(@PathVariable String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityExistsException("User " + username + " doesn't exist in database"));
    }

    // example F: other solution for example D
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/current")
    public User getUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityExistsException("Wrong username " + username + " in SecurityContexHolder"));
    }

    // example G: check tokenInterceptor service
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/token")
    public String checkRefreshToken() {
        return "Compare old and new token";
    }
}
