package com.example.carbooking.booking;

import com.example.carbooking.car.Car;
import com.example.carbooking.car.CarService;
import com.example.carbooking.exception.DuplicateResourceException;
import com.example.carbooking.exception.RequestValidationException;
import com.example.carbooking.exception.ResourceNotFoundException;
import com.example.carbooking.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class CarBookingService {
    private final CarBookingDao carBookingDao;
    private final CarService carService;
    private final UserService userService;

    public List<CarBooking> getAllBookings() {
        return carBookingDao.getAllBookings();
    }

    public void bookCar(CarBookingRegistrationRequest request) {
        List<CarBooking> bookings = getAllBookings();

        var car = carService.getCarById(request.car().getId());

        var user = userService.getUserById(request.user().getId());

        for (CarBooking booking : bookings) {
            if (!booking.isCanceled()){
                if (booking.getCar().equals(car)) {
                    throw new DuplicateResourceException("Car with id = [%s] is not available".formatted(car.getId()));
                }
            }
        }

        CarBooking carBooking = new CarBooking(
                car,
                user,
                request.bookingTime(),
                request.isCanceled()
        );

        carBookingDao.bookCar(carBooking);

    }

    public CarBooking getBookingById(Long id) {
        return carBookingDao
                .getBookingById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("No bookings with id = [%s]".formatted(id))
                );
    }

    public void cancelBooking(Long id) {
        var booking = getBookingById(id);

        if (booking.isCanceled()){
            throw new RequestValidationException("Booking with id = [%s] is already canceled".formatted(id));
        }

        booking.setCanceled(true);

        carBookingDao.cancelBooking(booking);
    }


    public List<Car> getAvailableCars() {
        List<Car> availableCars = carBookingDao.getAvailableCars().orElse(Collections.emptyList());
        if (availableCars.isEmpty()) {
            throw new ResourceNotFoundException("There are no available cars");
        }

        return availableCars;
    }

    public List<Car> getAvailableElectricCars() {
        List<Car> availableElectricCars = carBookingDao.getAvailableElectricCars().orElse(Collections.emptyList());

        if (availableElectricCars.isEmpty()) {
            throw new ResourceNotFoundException("There are no available electric cars");
        }

        return availableElectricCars;
    }

    public Set<Car> getAllUserBookedCars(Long userId) {
        Set<Car> userBookedCars = carBookingDao.getAllUserBookedCars(userId).orElse(Collections.emptySet());

        if (userBookedCars.isEmpty()) {
            throw new ResourceNotFoundException("User with id = [%s] has no booked cars".formatted(userId));
        }

        return userBookedCars;
    }
}
