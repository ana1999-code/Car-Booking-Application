package com.example.carbooking.car;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long>, JpaSpecificationExecutor<Car> {
    @Transactional
    @Modifying
    @Query("delete from Car c where c.id = :id")
    void deleteCarById(@Param("id") Long id);

    @Query("select c from Car c where c.isElectric = true")
    Optional<List<Car>> getAllElectricCars();

}
