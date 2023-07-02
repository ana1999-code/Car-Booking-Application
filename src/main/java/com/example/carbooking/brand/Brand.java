package com.example.carbooking.brand;

import com.example.carbooking.car.Car;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Table(
        name = "brand",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unique_brand",
                        columnNames = "brand_name"
                )
        }
)
@Entity(name = "Brand")
@NoArgsConstructor
@EqualsAndHashCode
public class Brand {

    @Id
    @SequenceGenerator(
            name = "brand_id_sequence",
            sequenceName = "brand_id_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "brand_id_sequence"
    )
    private Long id;

    @Column(
            name = "brand_name",
            nullable = false,
            updatable = false,
            columnDefinition = "TEXT"
    )
    private String brandName;

    @OneToMany(
            mappedBy = "brand",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private final List<Car> cars = new ArrayList<>();

    public Brand(String brandName) {
        this.brandName = brandName;
    }

    public Brand(Long id, String brandName) {
        this.id = id;
        this.brandName = brandName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public void addCar(Car car) {
        if (!cars.contains(car)) {
            cars.add(car);
        }
        car.setBrand(this);
    }

    @Override
    public String toString() {
        return "Brand{" +
                "id=" + id +
                ", brandName='" + brandName + '\'' +
                '}';
    }
}
