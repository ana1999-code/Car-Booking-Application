package com.example.carbooking.car;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(path = "${application.path}/cars")
@AllArgsConstructor
public class CarController {

    private final CarService carService;

    @GetMapping
    public List<Car> getAllCars(
            @RequestParam(required = false) BigDecimal lowerBound,
            @RequestParam(required = false) BigDecimal upperBound
    ) {
        return carService.getAllCars(lowerBound, upperBound);
    }

    @GetMapping("/electric")
    public List<Car> getAllElectricCars() {
        return carService.getAllElectricCars();
    }

    @GetMapping("/{id}")
    public Car getCarById(@PathVariable("id") Long id) {
        return carService.getCarById(id);
    }

    @PostMapping("/register")
    public void registerNewCar(@RequestBody CarRegistrationRequest request) {
        carService.registerNewCar(request);
    }

    @PutMapping("/{id}")
    public void updateCar(
            @PathVariable("id") Long id,
            @RequestBody CarUpdateRequest request
    ) {
        carService.updateCar(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteCar(@PathVariable("id") Long id) {
        carService.deleteCar(id);
    }

}
