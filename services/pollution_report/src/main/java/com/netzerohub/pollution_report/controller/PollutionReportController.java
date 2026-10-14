package com.netzerohub.pollution_report.controller;

import java.util.List;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netzerohub.common.annotation.RequireRole;
import com.netzerohub.common.dto.PageResponse;
import com.netzerohub.common.enums.AccountRole;

import com.netzerohub.pollution_report.dto.PollutionReportDto;
import com.netzerohub.pollution_report.form.CreatePollutionReportForm;
import com.netzerohub.pollution_report.form.GetPollutionReportsForm;
import com.netzerohub.pollution_report.form.UpdatePollutionReportStatusForm;
import com.netzerohub.pollution_report.service.PollutionReportService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/pollution-reports")
@RequireRole({ AccountRole.User, AccountRole.Admin })
@RequiredArgsConstructor
public class PollutionReportController {

    private final PollutionReportService reportService;

    @PostMapping
    public ResponseEntity<PollutionReportDto> createReport(
            @Valid @RequestBody CreatePollutionReportForm form) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reportService.createReport(form));
    }

    @RequireRole({ AccountRole.Admin })
    @GetMapping
    public ResponseEntity<PageResponse<PollutionReportDto>> getReports(
            @ParameterObject @Valid @ModelAttribute GetPollutionReportsForm form) {

        return ResponseEntity.ok(reportService.getReports(form));
    }

    @RequireRole({ AccountRole.User })
    @GetMapping("/my")
    public ResponseEntity<PageResponse<PollutionReportDto>> getMyReports(
            @ParameterObject @Valid @ModelAttribute GetPollutionReportsForm form) {

        return ResponseEntity.ok(reportService.getMyReports(form));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PollutionReportDto> getReportById(
            @PathVariable("id") Integer id) {

        return ResponseEntity.ok(reportService.getReportById(id));
    }

    @RequireRole({ AccountRole.Admin })
    @PutMapping("/status")
    public ResponseEntity<List<PollutionReportDto>> updateStatuses(
            @Valid @RequestBody UpdatePollutionReportStatusForm form) {

        return ResponseEntity.ok(reportService.updateStatuses(form));
    }
}