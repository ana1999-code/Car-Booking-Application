package com.example.carbooking.user;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    List<User> getAllUsers();

    Optional<User> getUserById(Long id);

    void registerNewUser(User user);

    boolean existsUserByEmail(String email);

    boolean existsUserByPhoneNumber(String phoneNumber);

    void updateUser(User user);

    void deleteUserById(Long id);
}
