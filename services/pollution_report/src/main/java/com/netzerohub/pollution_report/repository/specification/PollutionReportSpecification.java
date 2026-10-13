package com.netzerohub.pollution_report.repository.specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.data.jpa.domain.Specification;

import com.netzerohub.pollution_report.entity.PollutionReport;
import com.netzerohub.pollution_report.form.GetPollutionReportsForm;

import jakarta.persistence.criteria.Predicate;

public class PollutionReportSpecification {

    public static Specification<PollutionReport> filterByForm(
            GetPollutionReportsForm form) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (form.getType() != null) {
                predicates.add(
                        cb.equal(root.get("type"), form.getType()));
            }

            if (form.getStatus() != null) {
                predicates.add(
                        cb.equal(root.get("status"), form.getStatus()));
            }

            if (form.getSearch() != null
                    && !form.getSearch().isBlank()) {

                String keyword = form.getSearch()
                        .trim()
                        .toLowerCase(Locale.ROOT)
                        .replace("\\", "\\\\")
                        .replace("%", "\\%")
                        .replace("_", "\\_");

                String pattern = "%" + keyword + "%";

                predicates.add(cb.or(
                        cb.like(cb.lower(root.<String>get("title")),
                                pattern, '\\'),
                        cb.like(cb.lower(root.<String>get("description")),
                                pattern, '\\')));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<PollutionReport> ownedBy(
            Integer accountId) {

        return (root, query, cb) ->
                cb.equal(root.get("accountId"), accountId);
    }
}