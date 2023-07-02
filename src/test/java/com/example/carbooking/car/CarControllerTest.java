package com.example.carbooking.car;

import com.example.carbooking.brand.Brand;
import com.example.carbooking.exception.ResourceNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

@WebMvcTest(CarController.class)
class CarControllerTest {

    @MockBean
    private CarService carService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void itShouldGetAllCarsFromEndpoint() throws Exception {
        List<Car> cars = Arrays.asList(
                new Car(1L, "IL", new BigDecimal("10.00"), new Brand(1L, "BMW"), true),
                new Car(2L, "K", new BigDecimal("20.00"), new Brand(2L, "Mercedes"), false),
                new Car(3L, "BL", new BigDecimal("30.00"), new Brand(3L, "Audi"), true)
        );

        Mockito.when(carService.getAllCars(null, null)).thenReturn(cars);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/cars")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(Objects.requireNonNull(objectToJson(cars))));

        assertThat(carService.getAllCars(null, null)).hasSize(3);
        assertThat(carService.getAllCars(null, null).containsAll(cars)).isTrue();
    }

    @Test
    public void itShouldGetAllElectricCarsFromEndpoint() throws Exception {
        List<Car> cars = Arrays.asList(
                new Car(1L, "IL", new BigDecimal("10.00"), new Brand(1L, "BMW"), true),
                new Car(2L, "K", new BigDecimal("20.00"), new Brand(2L, "Mercedes"), false),
                new Car(3L, "BL", new BigDecimal("30.00"), new Brand(3L, "Audi"), true)
        );

        var electricCars = cars.stream().filter(Car::isElectric).collect(Collectors.toList());
        given(carService.getAllElectricCars()).willReturn(electricCars);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/cars/electric")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(Objects.requireNonNull(objectToJson(electricCars))));

        assertThat(carService.getAllElectricCars()).hasSize(2);
        assertThat(carService.getAllElectricCars().containsAll(electricCars)).isTrue();
    }

    @Test
    void itShouldGetCarByIdFromEndpoint() throws Exception {
        Car car = new Car(1L, "IL", new BigDecimal("10.00"), new Brand(1L, "BMW"), true);

        given(carService.getCarById(car.getId())).willReturn(car);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/cars/" + car.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(Objects.requireNonNull(objectToJson(car))));

        assertThat(carService.getCarById(car.getId())).isEqualTo(car);
    }

    @Test
    void itShouldThrowWhenGetCarByInvalidIdFromEndpoint() throws Exception {
        Car car = new Car(1L, "IL", new BigDecimal("10.00"), new Brand(1L, "BMW"), true);

        given(carService.getCarById(car.getId()))
                .willThrow(new ResourceNotFoundException(
                        "Car with id = [%s] not found.".formatted(car.getId())));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/cars/" + car.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Car with id = [%s] not found.".formatted(car.getId())));

        assertThatThrownBy(() -> carService.getCarById(car.getId()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Car with id = [%s] not found.".formatted(car.getId()));
    }

    @Test
    void itShouldRegisterNewCarOnEndpoint() throws Exception{
        CarRegistrationRequest request = new CarRegistrationRequest("IL", new BigDecimal("10.00"), new Brand(1L, "BMW"), true);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/cars/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(objectToJson(request))))
                .andExpect(MockMvcResultMatchers.status().isOk());

        ArgumentCaptor<CarRegistrationRequest> registrationRequestArgumentCaptor = ArgumentCaptor.forClass(CarRegistrationRequest.class);
        then(carService).should().registerNewCar(registrationRequestArgumentCaptor.capture());
        assertThat(registrationRequestArgumentCaptor.getValue()).isEqualTo(request);
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