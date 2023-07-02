package com.example.carbooking.car;

import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface CarDao {
    List<Car> getAllCars(Specification<Car> specification);

    Optional<Car> getCarById(Long id);

    Optional<List<Car>> getAllElectricCars();

    void registerNewCar(Car car);

    void updateCar(Car car);

    void deleteCar(Car car);

}
