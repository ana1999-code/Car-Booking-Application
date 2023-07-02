package com.example.carbooking.user;

import com.example.carbooking.booking.CarBookingService;
import com.example.carbooking.car.CarController;
import com.example.carbooking.exception.DuplicateResourceException;
import com.example.carbooking.exception.RequestValidationException;
import com.example.carbooking.exception.ResourceNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Objects;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void itShouldGetAllUsers() throws Exception {
        User user = new User(1L, "Maria", 23, "maria@gmailc.com", "098765432");

        given(userService.getAllUsers()).willReturn(List.of(user));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(Objects.requireNonNull(objectToJson(List.of(user)))));

        assertThat(userService.getAllUsers()).hasSize(1);
        assertThat(userService.getAllUsers().contains(user)).isTrue();
    }

    @Test
    void itShouldGetUserById() throws Exception {
        User user = new User(1L, "Maria", 23, "maria@gmailc.com", "098765432");

        given(userService.getUserById(user.getId())).willReturn(user);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(Objects.requireNonNull(objectToJson(user))));

        assertThat(userService.getUserById(user.getId())).isEqualTo(user);
    }

    @Test
    void itShouldThrowWhenGetUserWithInvalidId() throws Exception {
        var userId = 1L;

        given(userService.getUserById(userId))
                .willThrow(new ResourceNotFoundException(
                        "User with id = [%s] not found.".formatted(userId)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/users/" + 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User with id = [%s] not found.".formatted(userId)));

        assertThatThrownBy(() -> userService.getUserById(userId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User with id = [%s] not found.".formatted(userId));
    }

    @Test
    void itShouldRegisterNewUser() throws Exception {
        User user = new User(1L, "Maria", 23, "maria@gmailc.com", "098765432");

        UserRegistrationRequest registrationRequest = new UserRegistrationRequest(user);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(objectToJson(registrationRequest))))
                .andExpect(MockMvcResultMatchers.status().isOk());

        ArgumentCaptor<UserRegistrationRequest> requestArgumentCaptor = ArgumentCaptor.forClass(UserRegistrationRequest.class);
        then(userService).should().registerNewUser(requestArgumentCaptor.capture());
        assertThat(requestArgumentCaptor.getValue().getUser()).isEqualTo(registrationRequest.getUser());
    }

    //TODO: Refactor test
    @Test
    @Disabled
    void itShouldThrowWhenRegisterNewUserWithTakenEmail() throws Exception {
        var email = "maria@gmailc.com";
        User user = new User(1L, "Maria", 23, email, "098765432");

        UserRegistrationRequest registrationRequest = new UserRegistrationRequest(user);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(objectToJson(registrationRequest))))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Email [%s] is already taken".formatted(registrationRequest.getUser().getEmail())));
    }

    //TODO: Refactor test
    @Test
    @Disabled
    void itShouldThrowWhenRegisterUserWithAgeLessThanEighteen() throws Exception {
        User user = new User(1L, "Maria", 17, "maria@gmailc.com", "098765432");

        UserRegistrationRequest registrationRequest = new UserRegistrationRequest(user);

        RequestValidationException requestValidationException = mock(RequestValidationException.class);
        doThrow(requestValidationException)
                .when(userService)
                .registerNewUser(registrationRequest);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(objectToJson(registrationRequest))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User must be at least 18 years old, but the actual age is [%s]".formatted(registrationRequest.getUser().getAge())));
    }

    private String objectToJson(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            fail("Failed to convert object to json");
            return null;
        }
    }

}