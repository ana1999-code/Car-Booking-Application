package com.example.carbooking.user;

public record UserUpdateRequest(
        String name,
        Integer age,
        String email,
        String phoneNumber
) {
}
