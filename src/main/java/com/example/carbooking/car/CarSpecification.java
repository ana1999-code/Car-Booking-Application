package com.example.carbooking.car;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class CarSpecification {

    public static Specification<Car> specification(BigDecimal lowerBound, BigDecimal upperBound) {
        return new Specification<Car>() {
            @Override
            public Predicate toPredicate(Root<Car> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (lowerBound != null && upperBound != null) {
                    var rentalPrice = criteriaBuilder.between(root.get("rentalPrice"), lowerBound, upperBound);
                    query.where(rentalPrice).orderBy();
                }
                return null;
            }
        };
    }
}
