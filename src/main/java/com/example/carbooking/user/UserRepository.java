package com.example.carbooking.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select case when count(u)> 0 then true else false end from User u where email = :email")
    boolean existsUserByEmail(@Param("email") String email);

    @Query("select case when count(u)> 0 then true else false end from User u where phoneNumber = :phoneNumber")
    boolean existsUserByPhoneNumber(@Param("phoneNumber") String phoneNumber);

}
