package com.netzerohub.pollution_report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.netzerohub.pollution_report.entity.PollutionReport;

public interface IPollutionReportRepository
        extends JpaRepository<PollutionReport, Integer>,
        JpaSpecificationExecutor<PollutionReport> {
}   