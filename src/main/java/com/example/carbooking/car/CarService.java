package com.example.carbooking.car;

import com.example.carbooking.brand.Brand;
import com.example.carbooking.brand.BrandService;
import com.example.carbooking.exception.RequestValidationException;
import com.example.carbooking.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class CarService {

    public final CarDao carDao;

    public final BrandService brandService;


    public List<Car> getAllCars(BigDecimal lowerBound, BigDecimal upperBound) {
        var specification = CarSpecification.specification(lowerBound, upperBound);
        return carDao.getAllCars(specification);
    }

    public Car getCarById(Long id) {
        return carDao
                .getCarById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Car with id = [%s] not found.".formatted(id))
                );
    }

    public List<Car> getAllElectricCars() {
        List<Car> electricCars = carDao.getAllElectricCars().orElse(Collections.emptyList());
        if (electricCars.isEmpty()) {
            throw new ResourceNotFoundException("There are no electric cars");
        }

        return electricCars;
    }

    public void registerNewCar(CarRegistrationRequest request) {

        Brand brand = brandService.getBrandById(request.brand().getId());

        var car = new Car(
                request.regNumber(),
                request.rentalPrice(),
                brand,
                request.isElectric()
        );


        carDao.registerNewCar(car);
    }

    public void updateCar(Long id, CarUpdateRequest request) {
        var car = getCarById(id);
        boolean changes = false;

        if (request.regNumber() != null && !car.getRegNumber().equalsIgnoreCase(request.regNumber())) {
            car.setRegNumber(request.regNumber());
            changes = true;
        }

        if (request.brand() != null && !car.getBrand().getId().equals(request.brand().getId())) {
            Brand brand = brandService.getBrandById(request.brand().getId());
            car.setBrand(brand);
            changes = true;
        }

        if (request.rentalPrice() != null && !car.getRentalPrice().equals(request.rentalPrice())) {
            car.setRentalPrice(request.rentalPrice());
            changes = true;
        }

        if (!(request.isElectric() == car.isElectric())) {
            car.setElectric(request.isElectric());
            changes = true;
        }


        if (!changes) {
            throw new RequestValidationException("No data changes found.");
        }

        carDao.updateCar(car);
    }

    public void deleteCar(Long id) {
        var car = getCarById(id);

        carDao.deleteCar(car);
    }

}
