package com.example.carbooking.car;

import com.example.carbooking.brand.Brand;

import java.math.BigDecimal;

public record CarUpdateRequest(
        String regNumber,
        BigDecimal rentalPrice,
        Brand brand,
        Boolean isElectric
) {
}
