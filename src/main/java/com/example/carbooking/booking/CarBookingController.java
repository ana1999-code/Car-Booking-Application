package com.example.carbooking.booking;

import com.example.carbooking.car.Car;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("${application.path}/bookings")
@AllArgsConstructor
public class CarBookingController {

    private final CarBookingService carBookingService;

    @GetMapping
    public List<CarBooking> getAllBookings(){
        return carBookingService.getAllBookings();
    }

    @GetMapping("/{id}")
    public CarBooking getBookingById(@PathVariable("id") Long id){
        return carBookingService.getBookingById(id);
    }

    @PostMapping("/book")
    public void bookCar(@RequestBody CarBookingRegistrationRequest request){
        carBookingService.bookCar(request);
    }

    @PutMapping("/{id}")
    public void cancelBooking(@PathVariable("id")Long id){
        carBookingService.cancelBooking(id);
    }


    @GetMapping("/cars/available")
    public List<Car> getAvailableCars(){
        return carBookingService.getAvailableCars();
    }

    @GetMapping("/cars/available/electric")
    public List<Car> getAvailableElectricCars(){
        return carBookingService.getAvailableElectricCars();
    }

    @GetMapping("/{userId}/cars")
    public Set<Car> getAllUserBookedCars(@PathVariable("userId") Long userId){
        return carBookingService.getAllUserBookedCars(userId);
    }
}
