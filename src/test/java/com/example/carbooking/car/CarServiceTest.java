package com.example.carbooking.car;

import com.example.carbooking.brand.Brand;
import com.example.carbooking.brand.BrandService;
import com.example.carbooking.exception.RequestValidationException;
import com.example.carbooking.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class CarServiceTest {

    @Mock
    private CarDao carDao;

    @Mock
    private BrandService brandService;

    @Captor
    private ArgumentCaptor<Car> carArgumentCaptor;

    @InjectMocks
    private CarService underTest;

    @Test
    void itShouldGetAllCars() {
        var carId = 1L;
        Car car = new Car(carId, "IL", new BigDecimal("10.00"), new Brand(1L, "BMW"), true);

        given(carDao.getAllCars(any())).willReturn(List.of(car));

        var allCars = underTest.getAllCars(null, null);

        assertThat(allCars).hasSize(1);
        assertThat(allCars.contains(car)).isTrue();
    }

    @Test
    void itShouldGetCarById() {
        var carId = 1L;
        Car car = new Car(carId, "IL", new BigDecimal("10.00"), new Brand(1L, "BMW"), true);

        given(carDao.getCarById(carId)).willReturn(Optional.of(car));

        var underTestCarById = underTest.getCarById(carId);

        assertThat(underTestCarById).isEqualTo(car);
    }

    @Test
    void itShouldNotGetCarWithInvalidId() {
        var carId = 1L;

        given(carDao.getCarById(carId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.getCarById(carId))
                .hasMessageContaining("Car with id = [%s] not found.".formatted(carId))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void itShouldGetAllElectricCars() {
        var electricCarFirst = new Car(1L, "IL", new BigDecimal("10.00"), new Brand(1L, "BMW"), true);
        var electricCarSecond = new Car(3L, "IL", new BigDecimal("10.00"), new Brand(1L, "BMW"), true);
        List<Car> cars = List.of(
                electricCarFirst,
                new Car(2L, "IL", new BigDecimal("10.00"), new Brand(1L, "BMW"), false),
                electricCarSecond
        );

        given(carDao.getAllElectricCars()).willReturn(
                Optional.of(cars.stream().filter(Car::isElectric).collect(Collectors.toList()))
        );

        var electricCars = underTest.getAllElectricCars();

        assertThat(electricCars).hasSize(2);
        assertThat(electricCars.containsAll(List.of(electricCarFirst, electricCarSecond))).isTrue();
    }

    @Test
    void itShouldThrowWhenNoElectricCarsFound() {
        given(carDao.getAllElectricCars()).willReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.getAllElectricCars())
                .hasMessageContaining("There are no electric cars")
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void itShouldRegisterNewCar() {
        var carId = 1L;
        var brand = new Brand(1L, "BMW");
        Car car = new Car(carId, "IL", new BigDecimal("10.00"), brand, true);

        CarRegistrationRequest request = new CarRegistrationRequest(car.getRegNumber(), car.getRentalPrice(), car.getBrand(), car.isElectric());

        given(brandService.getBrandById(brand.getId())).willReturn(brand);

        underTest.registerNewCar(request);

        then(carDao).should().registerNewCar(carArgumentCaptor.capture());
        carArgumentCaptor.getValue().setId(carId);
        assertThat(carArgumentCaptor.getValue()).isEqualTo(car);
    }

    @Test
    void itShouldUpdateCarWhenRegNumberIsChanged() {
        var carId = 1L;
        var brand = new Brand(1L, "BMW");
        Car car = new Car(carId, "IL", new BigDecimal("10.00"), brand, true);

        given(carDao.getCarById(carId)).willReturn(Optional.of(car));

        var regNumber = "K";
        CarUpdateRequest request = new CarUpdateRequest(regNumber, car.getRentalPrice(), car.getBrand(), car.isElectric());

        underTest.updateCar(carId, request);

        then(carDao).should().updateCar(carArgumentCaptor.capture());
        carArgumentCaptor.getValue().setId(carId);
        assertThat(carArgumentCaptor.getValue()).isEqualTo(car);
        assertThat(carArgumentCaptor.getValue().getRegNumber()).isEqualTo(regNumber);
    }

    @Test
    void itShouldUpdateCarWhenBrandIsChanged() {
        var carId = 1L;
        Car car = new Car(carId, "IL", new BigDecimal("10.00"), new Brand(1L, "Mercedes"), true);

        var brandId = 2L;
        var brand = new Brand(brandId, "BMW");

        given(carDao.getCarById(carId)).willReturn(Optional.of(car));
        given(brandService.getBrandById(brandId)).willReturn(brand);

        CarUpdateRequest request = new CarUpdateRequest(car.getRegNumber(), car.getRentalPrice(), brand, car.isElectric());

        underTest.updateCar(carId, request);

        then(carDao).should().updateCar(carArgumentCaptor.capture());
        carArgumentCaptor.getValue().setId(carId);
        assertThat(carArgumentCaptor.getValue()).isEqualTo(car);
        assertThat(carArgumentCaptor.getValue().getBrand()).isEqualTo(brand);
    }

    @Test
    void itShouldUpdateCarWhenRentalPriceIsChanged() {
        var carId = 1L;
        Car car = new Car(carId, "IL", new BigDecimal("10.00"), new Brand(1L, "Mercedes"), true);

        given(carDao.getCarById(carId)).willReturn(Optional.of(car));

        var rentalPrice = new BigDecimal("30.00");
        CarUpdateRequest request = new CarUpdateRequest(car.getRegNumber(), rentalPrice, car.getBrand(), car.isElectric());

        underTest.updateCar(carId, request);

        then(carDao).should().updateCar(carArgumentCaptor.capture());
        carArgumentCaptor.getValue().setId(carId);
        assertThat(carArgumentCaptor.getValue()).isEqualTo(car);
        assertThat(carArgumentCaptor.getValue().getRentalPrice()).isEqualTo(rentalPrice);
    }

    @Test
    void itShouldUpdateCarWhenElectricFieldIsChanged() {
        var carId = 1L;
        Car car = new Car(carId, "IL", new BigDecimal("10.00"), new Brand(1L, "Mercedes"), true);

        given(carDao.getCarById(carId)).willReturn(Optional.of(car));

        var isElectric = false;
        CarUpdateRequest request = new CarUpdateRequest(car.getRegNumber(), car.getRentalPrice(), car.getBrand(), isElectric);

        underTest.updateCar(carId, request);

        then(carDao).should().updateCar(carArgumentCaptor.capture());
        carArgumentCaptor.getValue().setId(carId);
        assertThat(carArgumentCaptor.getValue()).isEqualTo(car);
        assertThat(carArgumentCaptor.getValue().isElectric()).isEqualTo(isElectric);
    }

    @Test
    void itShouldThrowWhenNoDataChanges() {
        var carId = 1L;
        Car car = new Car(carId, "IL", new BigDecimal("10.00"), new Brand(1L, "Mercedes"), true);

        given(carDao.getCarById(carId)).willReturn(Optional.of(car));

        CarUpdateRequest request = new CarUpdateRequest(car.getRegNumber(), car.getRentalPrice(), car.getBrand(), car.isElectric());

        assertThatThrownBy(() -> underTest.updateCar(carId, request))
                .hasMessageContaining("No data changes found.")
                .isInstanceOf(RequestValidationException.class);
    }

    @Test
    void itShouldDeleteCarById() {
        var carId = 1L;
        Car car = new Car(carId, "IL", new BigDecimal("10.00"), new Brand(1L, "Mercedes"), true);

        given(carDao.getCarById(carId)).willReturn(Optional.of(car));

        underTest.deleteCar(carId);

        assertThat(underTest.getAllCars(null, null)).isEmpty();
    }
}