package com.example.carbooking.car;

import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class CarJpaDataAccessService implements CarDao {

    private final CarRepository carRepository;

    @Override
    public List<Car> getAllCars(Specification<Car> specification) {
        return carRepository.findAll(specification);
    }

    @Override
    public Optional<Car> getCarById(Long id) {
        return carRepository.findById(id);
    }

    @Override
    public Optional<List<Car>> getAllElectricCars() {
        return carRepository.getAllElectricCars();
    }

    @Override
    public void registerNewCar(Car car) {
        carRepository.save(car);
    }

    @Override
    public void updateCar(Car car) {
        carRepository.save(car);
    }

    @Override
    public void deleteCar(Car car) {
        carRepository.deleteCarById(car.getId());
    }

}
