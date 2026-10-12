package com.wcc.recycling_centre.repository.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.wcc.recycling_centre.entity.Centre;
import com.wcc.recycling_centre.form.GetCentresForm;

import jakarta.persistence.criteria.Predicate;

public class CentreSpecification {

    public static Specification<Centre> filterByForm(GetCentresForm form) {
        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            // name
            if (form.getName() != null && !form.getName().trim().isEmpty()) {
                predicates.add(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + form.getName().trim().toLowerCase() + "%"
                    )
                );
            }

            return criteriaBuilder.and(
                predicates.toArray(new Predicate[0])
            );
        };
    }
}