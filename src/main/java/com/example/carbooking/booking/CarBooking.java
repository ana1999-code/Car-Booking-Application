package com.example.carbooking.booking;

import com.example.carbooking.car.Car;
import com.example.carbooking.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "car_booking")
@Entity(name = "CarBooking")
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CarBooking {

    @Id
    @SequenceGenerator(
            name = "booking_id_sequence",
            sequenceName = "booking_id_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "booking_id_sequence"
    )
    private Long id;

    @ManyToOne(
            cascade = CascadeType.PERSIST
    )
    @JoinColumn(
            name = "car_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "booking_car_fk"
            )
    )
    private Car car;

    @ManyToOne(
            cascade = CascadeType.PERSIST
    )
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "booking_user_fk"
            )
    )
    private User user;

    @Column(
            name = "booking_time",
            nullable = false
    )
    private LocalDateTime bookingTime;

    @Column(
            name = "is_canceled",
            nullable = false
    )
    private boolean isCanceled;

    public CarBooking(Car car, User user, LocalDateTime bookingTime, boolean isCanceled) {
        this.car = car;
        this.user = user;
        this.bookingTime = bookingTime;
        this.isCanceled = isCanceled;
    }


}
