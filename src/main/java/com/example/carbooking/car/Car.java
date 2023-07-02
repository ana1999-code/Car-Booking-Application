package com.example.carbooking.car;

import com.example.carbooking.booking.CarBooking;
import com.example.carbooking.brand.Brand;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Table(name = "car")
@Entity(name = "Car")
@EqualsAndHashCode
@NoArgsConstructor
public class Car {

    @Id
    @SequenceGenerator(
            name = "car_id_sequence",
            sequenceName = "car_id_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "car_id_sequence"
    )
    private Long id;

    @NotBlank(message = "Reg number must be not empty")
    @Column(
            name = "reg_number",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String regNumber;

    @NotNull(message = "Rental price must be not empty")
    @Column(
            name = "rental_price",
            nullable = false
    )
    private BigDecimal rentalPrice;

    @ManyToOne(
            cascade = CascadeType.PERSIST
    )
    @JoinColumn(
            name = "brand_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "car_brand_fk"
            )
    )
    private Brand brand;

    @NotNull(message = "Electric field is required")
    @Column(
            name = "is_electric",
            nullable = false
    )
    private Boolean isElectric;

    @OneToMany(
            mappedBy = "car",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private final List<CarBooking> bookings = new ArrayList<>();

    public Car(String regNumber, BigDecimal rentalPrice, Brand brand, Boolean isElectric) {
        this.regNumber = regNumber;
        this.rentalPrice = rentalPrice;
        this.brand = brand;
        this.isElectric = isElectric;
    }

    public Car(Long id, String regNumber, BigDecimal rentalPrice, Brand brand, Boolean isElectric) {
        this.id = id;
        this.regNumber = regNumber;
        this.rentalPrice = rentalPrice;
        this.brand = brand;
        this.isElectric = isElectric;
    }

    public void addBooking(CarBooking carBooking) {
        if (!bookings.contains(carBooking)) {
            bookings.add(carBooking);
        }
        carBooking.setCar(this);
        carBooking.setCanceled(false);
    }

    public void removeBooking(CarBooking carBooking) {
        bookings.remove(carBooking);
        carBooking.setCar(null);
        carBooking.setCanceled(true);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    public BigDecimal getRentalPrice() {
        return rentalPrice;
    }

    public void setRentalPrice(BigDecimal rentalPrice) {
        this.rentalPrice = rentalPrice;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    @JsonProperty("isElectric")
    public boolean isElectric() {
        return isElectric;
    }

    public void setElectric(boolean electric) {
        isElectric = electric;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", regNumber='" + regNumber + '\'' +
                ", rentalPrice=" + rentalPrice +
                ", brand=" + brand +
                ", isElectric=" + isElectric +
                '}';
    }
}
