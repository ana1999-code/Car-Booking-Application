package com.example.carbooking.booking;

import com.example.carbooking.car.Car;
import com.example.carbooking.user.User;

import java.time.LocalDateTime;

public record CarBookingRegistrationRequest(
        Car car,
        User user,
        LocalDateTime bookingTime,
        boolean isCanceled
) {
}
