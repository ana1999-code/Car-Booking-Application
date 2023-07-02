package com.example.carbooking.user;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${application.path}/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") Long id) {
        return userService.getUserById(id);
    }

    @PostMapping("/register")
    public void registerNewUser(@Valid @RequestBody UserRegistrationRequest request) {
        userService.registerNewUser(request);
    }

    @PutMapping("/{id}")
    public void updateUser(
            @PathVariable("id") Long id,
            @Valid @RequestBody UserUpdateRequest request
    ) {
        userService.updateUser(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable("id") Long id) {
        userService.deleteUserById(id);
    }
}
