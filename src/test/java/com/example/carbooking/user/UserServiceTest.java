package com.example.carbooking.user;

import com.example.carbooking.exception.DuplicateResourceException;
import com.example.carbooking.exception.RequestValidationException;
import com.example.carbooking.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService underTest;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Test
    void itShouldGetAllUsers() {
        List<User> userList = List.of(
                new User(1L, "Maria", 23, "maria@mail.com", "098234921"),
                new User(2L, "John", 23, "john@mail.com", "098234901"),
                new User(3L, "Ali", 23, "ali@mail.com", "098004921")
        );

        given(userDao.getAllUsers()).willReturn(userList);

        var underTestAllUsers = underTest.getAllUsers();

        assertThat(underTestAllUsers).hasSize(3);
        assertThat(underTestAllUsers.containsAll(userList)).isTrue();

    }

    @Test
    void itShouldGetUserById() {
        var userId = 1L;
        var userById = new User(userId, "Maria", 23, "maria@mail.com", "098234921");

        given(userDao.getUserById(userId)).willReturn(Optional.of(userById));

        var underTestUserById = underTest.getUserById(userId);

        assertThat(underTestUserById).isEqualTo(userById);
        assertThat(underTestUserById.getId()).isEqualTo(userId);
    }

    @Test
    void itShouldThrowWhenGetUserWithInvalidId() {
        var userId = 1L;

        given(userDao.getUserById(userId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.getUserById(userId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User with id = [%s] does not exist".formatted(userId));
    }

    @Test
    void itShouldRegisterNewUser() {
        var userId = 1L;

        User user = new User(userId, "Maria", 23, "maria@mail.com", "098765432");

        UserRegistrationRequest request = new UserRegistrationRequest(user);

        underTest.registerNewUser(request);

        then(userDao).should().registerNewUser(userArgumentCaptor.capture());
        userArgumentCaptor.getValue().setId(userId);
        assertThat(userArgumentCaptor.getValue()).isEqualTo(user);
    }

    @Test
    void itShouldNotRegisterNewUserWithExistingEmail() {
        var userEmail = "maria@gmail.com";
        User user = new User(1L, "Maria", 23, userEmail, "098765432");

        UserRegistrationRequest request = new UserRegistrationRequest(user);

        given(userDao.existsUserByEmail(any())).willReturn(true);

        assertThatThrownBy(() -> underTest.registerNewUser(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Email [%s] is already taken".formatted(userEmail));
    }

    @Test
    void itShouldNotRegisterNewUserWithExistingPhoneNumber() {
        var phoneNumber = "098765432";
        User user = new User(1L, "Maria", 23, "maria@mail.com", phoneNumber);

        UserRegistrationRequest request = new UserRegistrationRequest(user);

        given(userDao.existsUserByPhoneNumber(any())).willReturn(true);

        assertThatThrownBy(() -> underTest.registerNewUser(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Phone Number [%s] is already taken".formatted(phoneNumber));
    }

    @Test
    void itShouldNotRegisterNewUserWithAgeLessThanEighteen() {
        var age = 17;
        User user = new User(1L, "Maria", age, "maria@mail.com", "098765432");

        UserRegistrationRequest request = new UserRegistrationRequest(user);

        assertThatThrownBy(() -> underTest.registerNewUser(request))
                .isInstanceOf(RequestValidationException.class)
                .hasMessageContaining("User must be at least 18 years old, but the actual age is [%s]".formatted(age));
    }
}