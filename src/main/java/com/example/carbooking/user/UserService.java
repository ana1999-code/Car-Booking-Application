package com.example.carbooking.user;

import com.example.carbooking.exception.DuplicateResourceException;
import com.example.carbooking.exception.RequestValidationException;
import com.example.carbooking.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private final UserDao userDao;

    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    public User getUserById(Long id) {
        return userDao
                .getUserById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User with id = [%s] does not exist".formatted(id))
                );
    }

    public void registerNewUser(UserRegistrationRequest request) {

        if (userDao.existsUserByEmail(request.getUser().getEmail())) {
            throw new DuplicateResourceException("Email [%s] is already taken".formatted(request.getUser().getEmail()));
        }

        if (userDao.existsUserByPhoneNumber(request.getUser().getPhoneNumber())) {
            throw new DuplicateResourceException("Phone Number [%s] is already taken".formatted(request.getUser().getPhoneNumber()));
        }

        if (request.getUser().getAge() < 18) {
            throw new RequestValidationException("User must be at least 18 years old, but the actual age is [%s]".formatted(request.getUser().getAge()));
        }

        var user = new User(
                request.getUser().getName(),
                request.getUser().getAge(),
                request.getUser().getEmail(),
                request.getUser().getPhoneNumber()
        );

        userDao.registerNewUser(user);
    }

    public void updateUser(Long id, UserUpdateRequest request) {
        var user = getUserById(id);
        boolean changes = false;

        if (userDao.existsUserByEmail(request.email())) {
            throw new DuplicateResourceException("Email [%s] is already taken".formatted(request.email()));
        }

        if (userDao.existsUserByPhoneNumber(request.phoneNumber())) {
            throw new DuplicateResourceException("Phone Number [%s] is already taken".formatted(request.phoneNumber()));
        }

        if (request.age() < 18) {
            throw new RequestValidationException("User must be at least 18 years old, but the actual age is [%s]".formatted(request.age()));
        }

        if (request.name() != null && !user.getName().equalsIgnoreCase(request.name())) {
            user.setName(request.name());
            changes = true;
        }

        if (request.age() != null && !user.getAge().equals(request.age())) {
            user.setAge(request.age());
            changes = true;
        }

        if (request.email() != null && !user.getEmail().equals(request.email())) {
            user.setEmail(request.email());
            changes = true;
        }

        if (request.phoneNumber() != null && !user.getPhoneNumber().equals(request.phoneNumber())) {
            user.setPhoneNumber(request.phoneNumber());
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("No data changes found.");
        }

        userDao.updateUser(user);
    }

    public void deleteUserById(Long id) {

        if (!userDao.getUserById(id).isPresent()) {
            throw new ResourceNotFoundException("User with id = [%s] not found".formatted(id));
        }

        userDao.deleteUserById(id);
    }
}
