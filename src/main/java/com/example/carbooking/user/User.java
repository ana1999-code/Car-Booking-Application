package com.example.carbooking.user;

import com.example.carbooking.booking.CarBooking;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_user_constraint", columnNames = {"email", "phone_number"})
        }
)
@Entity(name = "User")
@NoArgsConstructor
@EqualsAndHashCode
public class User {

    @Id
    @SequenceGenerator(
            name = "user_id_sequence",
            sequenceName = "user_id_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_id_sequence"
    )
    private Long id;

    @Column(
            name = "name",
            nullable = false
    )
    private String name;

    @Min(
            value = 18,
            message = "User must be at least 18 years old"
    )
    @Column(
            name = "age",
            nullable = false
    )
    private Integer age;

    @Email(message = "Email is not valid")
    @Column(
            name = "email",
            nullable = false
    )
    private String email;

    @Pattern(regexp = "(\\+32|0)[0-9]{8}")
    @Column(
            name = "phone_number",
            nullable = false
    )
    private String phoneNumber;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private final List<CarBooking> bookings = new ArrayList<>();

    public User(String name, Integer age, String email, String phoneNumber) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public User(Long id, String name, Integer age, String email, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public void addBooking(CarBooking carBooking) {
        if (!bookings.contains(carBooking)) {
            bookings.add(carBooking);
        }
        carBooking.setUser(this);
        carBooking.setCanceled(false);
    }

    public void removeBooking(CarBooking carBooking) {
        bookings.remove(carBooking);
        carBooking.setUser(null);
        carBooking.setCanceled(true);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

