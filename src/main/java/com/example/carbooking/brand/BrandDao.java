package com.example.carbooking.brand;

import java.util.List;
import java.util.Optional;

public interface BrandDao {
    List<Brand> getAllBrands();

    Optional<Brand> getBrandById(Long id);

    Boolean existBrandWithName(String name);

    void registerNewBrand(Brand brand);

    void deleteBrandById(Long id);
}
