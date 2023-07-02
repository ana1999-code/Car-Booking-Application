package com.example.carbooking.booking;

import com.example.carbooking.car.Car;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CarBookingDao {
    List<CarBooking> getAllBookings();

    void bookCar(CarBooking carBooking);

    Optional<CarBooking> getBookingById(Long id);

    void cancelBooking(CarBooking carBooking);

    Optional<List<Car>> getAvailableCars();

    Optional<List<Car>> getAvailableElectricCars();

    Optional<Set<Car>> getAllUserBookedCars(Long userId);
}
