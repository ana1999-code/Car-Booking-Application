package com.example.carbooking.brand;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    @Query("select b from Brand b where b.brandName = :brandName")
    Optional<Brand> findByBrandName(@Param("brandName") String brandName);
}
