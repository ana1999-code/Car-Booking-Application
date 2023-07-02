package com.example.carbooking.booking;

import com.example.carbooking.car.Car;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@AllArgsConstructor
public class CarBookingDataAccessService implements CarBookingDao{
    private final CarBookingRepository carBookingRepository;

    @Override
    public List<CarBooking> getAllBookings() {
        return carBookingRepository.findAll();
    }

    @Override
    public void bookCar(CarBooking carBooking) {
        carBookingRepository.save(carBooking);
    }

    @Override
    public Optional<CarBooking> getBookingById(Long id) {
        return carBookingRepository.findById(id);
    }

    @Override
    public void cancelBooking(CarBooking carBooking) {
        carBookingRepository.save(carBooking);
    }


    @Override
    public Optional<List<Car>> getAvailableCars() {
        return carBookingRepository.getAvailableCars();
    }

    @Override
    public Optional<List<Car>> getAvailableElectricCars() {
        return carBookingRepository.getAvailableElectricCars();
    }

    @Override
    public Optional<Set<Car>> getAllUserBookedCars(Long userId) {
        return carBookingRepository.getAllUserBookedCars(userId);
    }
}
