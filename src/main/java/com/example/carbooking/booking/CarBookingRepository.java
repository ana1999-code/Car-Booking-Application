package com.example.carbooking.booking;

import com.example.carbooking.car.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CarBookingRepository extends JpaRepository<CarBooking, Long> {
    @Transactional
    @Modifying
    @Query("delete from CarBooking cb where cb.id = :id")
    void deleteCarBookingById(@Param("id") Long id);

    @Query("select cb from CarBooking cb inner join cb.car c where c.id = :id")
    List<CarBooking> findAllByCarId(@Param("id")Long id);

    @Query("select cb from CarBooking cb inner join cb.user u where u.id = :id")
    List<CarBooking> findAllByUserId(@Param("id")Long id);


    @Query("SELECT c FROM CarBooking cb RIGHT JOIN cb.car c WHERE cb IS NULL OR cb.isCanceled = true")
    Optional<List<Car>> getAvailableCars();

    @Query("SELECT c FROM CarBooking cb RIGHT JOIN cb.car c WHERE c.isElectric = true AND (cb IS NULL OR cb.isCanceled = true)")
    Optional<List<Car>> getAvailableElectricCars();

    @Query("SELECT c FROM CarBooking cb INNER JOIN cb.car c INNER JOIN cb.user u WHERE u.id = :userId")
    Optional<Set<Car>> getAllUserBookedCars(@Param("userId") Long userId);

}
