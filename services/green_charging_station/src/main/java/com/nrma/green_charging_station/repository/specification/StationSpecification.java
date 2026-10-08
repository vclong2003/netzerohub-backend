package com.nrma.green_charging_station.repository.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.nrma.green_charging_station.entity.Plug;
import com.nrma.green_charging_station.entity.Station;
import com.nrma.green_charging_station.form.GetChargingStationsForm;

import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

public class StationSpecification {
    public static Specification<Station> filterByForm(GetChargingStationsForm form) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // name
            if (form.getName() != null && !form.getName().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + form.getName() + "%"));
            }

            // status
            if (form.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), form.getStatus()));
            }

            // renewableEnergyPercent
            if (form.getRenewableEnergyPercent() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("renewableEnergyPercent"),
                        form.getRenewableEnergyPercent()));
            }

            // longitude, latitude
            if (form.getLongitude() != null && form.getLatitude() != null) {
                predicates.add(criteriaBuilder.and(
                        criteriaBuilder.greaterThanOrEqualTo(root.get("longitude"), form.getLongitude()),
                        criteriaBuilder.lessThanOrEqualTo(root.get("longitude"), form.getLongitude()),
                        criteriaBuilder.greaterThanOrEqualTo(root.get("latitude"), form.getLatitude()),
                        criteriaBuilder.lessThanOrEqualTo(root.get("latitude"), form.getLatitude())));
            }

            // plugType
            if (form.getPlugType() != null) {
                Subquery<Integer> subquery = query.subquery(Integer.class);
                Root<Plug> plugRoot = subquery.from(Plug.class);

                subquery.select(criteriaBuilder.literal(1))
                        .where(
                                criteriaBuilder.equal(plugRoot.get("station"), root),
                                criteriaBuilder.equal(plugRoot.get("type"), form.getPlugType()));

                predicates.add(criteriaBuilder.exists(subquery));
            }

            return criteriaBuilder.conjunction();
        };
    }
}
