package com.example.carbooking.booking;

import com.example.carbooking.brand.Brand;
import com.example.carbooking.car.Car;
import com.example.carbooking.car.CarService;
import com.example.carbooking.exception.DuplicateResourceException;
import com.example.carbooking.exception.RequestValidationException;
import com.example.carbooking.exception.ResourceNotFoundException;
import com.example.carbooking.user.User;
import com.example.carbooking.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class CarBookingServiceTest {

    @Mock
    private CarBookingDao carBookingDao;

    @Mock
    private UserService userService;

    @Mock
    private CarService carService;

    @InjectMocks
    private CarBookingService underTest;

    @Captor
    private ArgumentCaptor<CarBooking> carBookingArgumentCaptor;

    @Test
    void itShouldGetAllBookings() {
        CarBooking carBooking = new CarBooking(1L,
                new Car(1L, "IL", new BigDecimal("10.00"), new Brand(1L, "BMW"), true),
                new User(1L, "Maria", 23, "maria@gmail.com", "09876543"),
                LocalDateTime.now(),
                false);

        given(carBookingDao.getAllBookings()).willReturn(List.of(carBooking));

        var underTestAllBookings = underTest.getAllBookings();

        assertThat(underTestAllBookings).hasSize(1);
        assertThat(underTestAllBookings.contains(carBooking)).isTrue();
    }

    @Test
    void itShouldGetBookingById() {
        var bookingId = 1L;
        CarBooking carBooking = new CarBooking(bookingId,
                new Car(1L, "IL", new BigDecimal("10.00"), new Brand(1L, "BMW"), true),
                new User(1L, "Maria", 23, "maria@gmail.com", "09876543"),
                LocalDateTime.now(),
                false);

        given(carBookingDao.getBookingById(bookingId)).willReturn(Optional.of(carBooking));

        var underTestBookingById = underTest.getBookingById(bookingId);

        assertThat(underTestBookingById).isEqualTo(carBooking);
    }

    @Test
    void itShouldThrowWhenGetBookingByInvalidId() {
        var bookingId = 1L;

        given(carBookingDao.getBookingById(bookingId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.getBookingById(bookingId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("No bookings with id = [%s]".formatted(bookingId));
    }

    @Test
    void itShouldBookCar() {
        var bookingId = 1L;
        var car = new Car(1L, "IL", new BigDecimal("10.00"), new Brand(1L, "BMW"), true);
        var user = new User(1L, "Maria", 23, "maria@gmail.com", "09876543");
        CarBooking carBooking = new CarBooking(bookingId,
                car,
                user,
                LocalDateTime.now(),
                false);

        given(userService.getUserById(user.getId())).willReturn(user);
        given(carService.getCarById(car.getId())).willReturn(car);

        CarBookingRegistrationRequest request = new CarBookingRegistrationRequest(
                car,
                user,
                carBooking.getBookingTime(),
                carBooking.isCanceled()
        );

        underTest.bookCar(request);

        then(carBookingDao).should().bookCar(carBookingArgumentCaptor.capture());
        carBookingArgumentCaptor.getValue().setId(bookingId);
        assertThat(carBookingArgumentCaptor.getValue()).isEqualTo(carBooking);
        assertThat(carBookingArgumentCaptor.getValue().getCar()).isEqualTo(car);
        assertThat(carBookingArgumentCaptor.getValue().getUser()).isEqualTo(user);
    }

    @Test
    void itShouldBookCarWhenItIsCanceled() {
        var bookingId = 1L;
        var car = new Car(1L, "IL", new BigDecimal("10.00"), new Brand(1L, "BMW"), true);
        var user = new User(1L, "Maria", 23, "maria@gmail.com", "09876543");
        CarBooking carBooking = new CarBooking(
                bookingId,
                car,
                user,
                LocalDateTime.now(),
                true
        );

        given(userService.getUserById(user.getId())).willReturn(user);
        given(carService.getCarById(car.getId())).willReturn(car);
        given(carBookingDao.getAllBookings()).willReturn(List.of(carBooking));

        CarBookingRegistrationRequest request = new CarBookingRegistrationRequest(
                car,
                user,
                carBooking.getBookingTime(),
                carBooking.isCanceled()
        );
        underTest.bookCar(request);

        then(carBookingDao).should().bookCar(carBookingArgumentCaptor.capture());
        carBookingArgumentCaptor.getValue().setId(bookingId);
        assertThat(carBookingArgumentCaptor.getValue()).isEqualTo(carBooking);
    }

    @Test
    void itShouldNotBookCarWhenItIsNotAvailable() {
        var bookingId = 1L;
        var car = new Car(1L, "IL", new BigDecimal("10.00"), new Brand(1L, "BMW"), true);
        var user = new User(1L, "Maria", 23, "maria@gmail.com", "09876543");
        CarBooking carBooking = new CarBooking(
                bookingId,
                car,
                user,
                LocalDateTime.now(),
                false
        );

        given(userService.getUserById(user.getId())).willReturn(user);
        given(carService.getCarById(car.getId())).willReturn(car);
        given(carBookingDao.getAllBookings()).willReturn(List.of(carBooking));

        CarBookingRegistrationRequest request = new CarBookingRegistrationRequest(
                car,
                user,
                carBooking.getBookingTime(),
                carBooking.isCanceled()
        );

        assertThatThrownBy(() -> underTest.bookCar(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Car with id = [%s] is not available".formatted(car.getId()));
    }

    @Test
    void itShouldCancelBooking() {
        var bookingId = 1L;
        var car = new Car(1L, "IL", new BigDecimal("10.00"), new Brand(1L, "BMW"), true);
        var user = new User(1L, "Maria", 23, "maria@gmail.com", "09876543");
        CarBooking carBooking = new CarBooking(
                bookingId,
                car,
                user,
                LocalDateTime.now(),
                false
        );

        given(carBookingDao.getBookingById(bookingId)).willReturn(Optional.of(carBooking));

        underTest.cancelBooking(bookingId);

        then(carBookingDao).should().cancelBooking(carBookingArgumentCaptor.capture());
        assertThat(carBookingArgumentCaptor.getValue().isCanceled()).isTrue();
    }

    @Test
    void itShouldThrowWhenTryingToCancelBookkingThatIsAlreadyCanceled() {
        var bookingId = 1L;
        var car = new Car(1L, "IL", new BigDecimal("10.00"), new Brand(1L, "BMW"), true);
        var user = new User(1L, "Maria", 23, "maria@gmail.com", "09876543");
        CarBooking carBooking = new CarBooking(
                bookingId,
                car,
                user,
                LocalDateTime.now(),
                true
        );

        given(carBookingDao.getBookingById(bookingId)).willReturn(Optional.of(carBooking));

        assertThatThrownBy(() -> underTest.cancelBooking(bookingId))
                .isInstanceOf(RequestValidationException.class)
                .hasMessageContaining("Booking with id = [%s] is already canceled".formatted(bookingId));
    }

    @Test
    void itShouldGetAvailableCars() {
        var carOne = new Car(1L, "IL", new BigDecimal("10.00"), new Brand(1L, "BMW"), false);
        var carTwo = new Car(1L, "IL", new BigDecimal("10.00"), new Brand(1L, "BMW"), true);

        given(carBookingDao.getAvailableCars()).willReturn(Optional.of(List.of(carOne, carTwo)));

        var underTestAvailableCars = underTest.getAvailableCars();

        assertThat(underTestAvailableCars).hasSize(2);
        assertThat(underTestAvailableCars.containsAll(List.of(carOne, carTwo))).isTrue();
    }

    @Test
    void itShouldThrowWhenThereAreNoAvailableCars() {
        assertThatThrownBy(() -> underTest.getAvailableCars())
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("There are no available cars");
    }

    @Test
    void itShouldGetAvailableElectricCars() {
        var carOne = new Car(1L, "IL", new BigDecimal("10.00"), new Brand(1L, "BMW"), false);
        var carTwo = new Car(1L, "IL", new BigDecimal("10.00"), new Brand(1L, "BMW"), true);

        given(carBookingDao.getAvailableElectricCars()).willReturn(Optional.of(List.of(carTwo)));

        var underTestAvailableCars = underTest.getAvailableElectricCars();

        assertThat(underTestAvailableCars).hasSize(1);
        assertThat(underTestAvailableCars.contains(carTwo)).isTrue();
        assertThat(underTestAvailableCars.contains(carOne)).isFalse();
    }

    @Test
    void itShouldThrowWhenThereAreNoAvailableElectricCars() {
        assertThatThrownBy(() -> underTest.getAvailableElectricCars())
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("There are no available electric cars");
    }

    @Test
    void itShouldGetAllUserBookedCars() {
        var bookingId = 1L;
        var car = new Car(1L, "IL", new BigDecimal("10.00"), new Brand(1L, "BMW"), true);
        var user = new User(1L, "Maria", 23, "maria@gmail.com", "09876543");
        CarBooking carBooking = new CarBooking(
                bookingId,
                car,
                user,
                LocalDateTime.now(),
                true
        );

        given(carBookingDao.getAllUserBookedCars(user.getId())).willReturn(Optional.of(Set.of(car)));

        var testAllUserBookedCars = underTest.getAllUserBookedCars(user.getId());

        assertThat(testAllUserBookedCars).hasSize(1);
        assertThat(testAllUserBookedCars.contains(car)).isTrue();

    }

    @Test
    void itShouldThrowWhenNoBookedCarsForUser() {
        var user = new User(1L, "Maria", 23, "maria@gmail.com", "09876543");

        given(carBookingDao.getAllUserBookedCars(user.getId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.getAllUserBookedCars(user.getId()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User with id = [%s] has no booked cars".formatted(user.getId()));
    }
}