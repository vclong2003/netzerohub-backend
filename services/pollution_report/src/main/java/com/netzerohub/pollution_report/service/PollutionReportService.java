package com.netzerohub.pollution_report.service;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.netzerohub.common.client.IAuthClient;
import com.netzerohub.common.context.RequestContext;
import com.netzerohub.common.dto.AccountDto;
import com.netzerohub.common.dto.PageResponse;
import com.netzerohub.common.enums.AccountRole;
import com.netzerohub.common.enums.ErrorCode;
import com.netzerohub.common.exception.BadRequestException;
import com.netzerohub.common.util.StorageUtil;
import com.netzerohub.pollution_report.config.RabbitMQProperties;
import com.netzerohub.pollution_report.dto.PollutionReportDto;
import com.netzerohub.pollution_report.dto.PollutionReportUpdateEvent;
import com.netzerohub.pollution_report.entity.PollutionReport;
import com.netzerohub.pollution_report.enums.PollutionReportStatus;
import com.netzerohub.pollution_report.form.CreatePollutionReportForm;
import com.netzerohub.pollution_report.form.GetPollutionReportsForm;
import com.netzerohub.pollution_report.form.UpdatePollutionReportStatusForm;
import com.netzerohub.pollution_report.repository.IPollutionReportRepository;
import com.netzerohub.pollution_report.repository.specification.PollutionReportSpecification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PollutionReportService {

        private final IPollutionReportRepository reportRepository;
        private final StorageUtil storageUtil;
        private final RabbitTemplate rabbitTemplate;
        private final RabbitMQProperties rabbitMQProperties;
        private final IAuthClient authClient;

        private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter
                        .ofPattern("dd MMM yyyy, HH:mm '('z')'", Locale.ENGLISH)
                        .withZone(ZoneId.of("Australia/Sydney"));

        @Transactional
        public PollutionReportDto createReport(
                        CreatePollutionReportForm form) {

                RequestContext context = requireLogin();

                PollutionReport report = new PollutionReport();
                report.setAccountId(context.getAccountId());
                report.setTitle(form.getTitle().trim());
                report.setDescription(form.getDescription().trim());
                report.setType(form.getType());
                report.setStatus(PollutionReportStatus.PENDING);
                report.setImageUrl(trimOrNull(form.getImageUrl()));
                report.setAddress(trimOrNull(form.getAddress()));

                if (form.getCoordinates() != null) {
                        report.setLatitude(form.getCoordinates().getLatitude());
                        report.setLongitude(form.getCoordinates().getLongitude());
                }

                return toDto(reportRepository.saveAndFlush(report));
        }

        @Transactional(readOnly = true)
        public PageResponse<PollutionReportDto> getReports(
                        GetPollutionReportsForm form) {

                requireAdmin();

                Specification<PollutionReport> specification = PollutionReportSpecification.filterByForm(form);

                return queryReports(form, specification);
        }

        @Transactional(readOnly = true)
        public PageResponse<PollutionReportDto> getMyReports(
                        GetPollutionReportsForm form) {

                RequestContext context = requireLogin();

                Specification<PollutionReport> specification = PollutionReportSpecification.filterByForm(form)
                                .and(PollutionReportSpecification.ownedBy(
                                                context.getAccountId()));

                return queryReports(form, specification);
        }

        @Transactional(readOnly = true)
        public PollutionReportDto getReportById(Integer id) {

                RequestContext context = requireLogin();
                PollutionReport report = findReport(id);

                boolean isAdmin = context.getAccountRole() == AccountRole.Admin;

                boolean isOwner = context.getAccountId().equals(report.getAccountId());

                if (!isAdmin && !isOwner) {
                        throw new BadRequestException(
                                        ErrorCode.ResourceNotOwnedByUser,
                                        "You cannot access another user's report");
                }

                return toDto(report);
        }

        @Transactional
        public List<PollutionReportDto> updateStatuses(
                        UpdatePollutionReportStatusForm form) {

                requireAdmin();

                List<Integer> ids = form.getIds().stream()
                                .distinct()
                                .toList();

                List<PollutionReport> reports = reportRepository.findAllById(ids);

                if (reports.size() != ids.size()) {
                        throw new BadRequestException(
                                        ErrorCode.ResourceNotFound,
                                        "One or more reports were not found");
                }

                reports.forEach(report -> report.setStatus(form.getStatus()));
                List<PollutionReport> savedReports = reportRepository.saveAllAndFlush(reports);

                // filter duplicate accounts
                Map<Integer, AccountDto> accounts = reports.stream()
                                .map(PollutionReport::getAccountId)
                                .distinct()
                                .collect(Collectors.toMap(
                                                accountId -> accountId,
                                                accountId -> authClient.getAccountById(accountId)));

                List<PollutionReportUpdateEvent> events = reports.stream()
                                .map(report -> {
                                        AccountDto account = accounts.get(report.getAccountId());
                                        if (account == null) {
                                                log.warn("Account not found for report {}", report.getId());
                                                return null;
                                        }
                                        return new PollutionReportUpdateEvent(
                                                        account.getEmail(),
                                                        account.getFirstName() + " " + account.getLastName(),
                                                        report.getTitle(),
                                                        DATE_FORMATTER.format(report.getCreatedAt()));
                                })
                                .filter(event -> Objects.nonNull(event))
                                .toList();

                events.forEach(event -> {
                        try {
                                rabbitTemplate.convertAndSend(
                                                rabbitMQProperties.exchange().notification(),
                                                rabbitMQProperties.routingKey().notification().pollutionReportUpdate(),
                                                event);
                        } catch (Exception e) {
                                log.error("Error sending notification for report {}", event.getReportTitle(), e);
                        }
                });

                return savedReports.stream()
                                .map(this::toDto)
                                .toList();
        }

        private PageResponse<PollutionReportDto> queryReports(
                        GetPollutionReportsForm form,
                        Specification<PollutionReport> specification) {

                PageRequest pageable = PageRequest.of(
                                form.getPage(),
                                form.getPageSize(),
                                Sort.by(Sort.Direction.DESC, "createdAt", "id"));

                Page<PollutionReport> result = reportRepository.findAll(specification, pageable);

                return PageResponse.<PollutionReportDto>builder()
                                .data(result.getContent().stream()
                                                .map(this::toDto)
                                                .toList())
                                .page(result.getNumber())
                                .pageSize(result.getSize())
                                .totalElements(result.getTotalElements())
                                .totalPages(result.getTotalPages())
                                .hasNext(result.hasNext())
                                .hasPrevious(result.hasPrevious())
                                .build();
        }

        private PollutionReport findReport(Integer id) {
                return reportRepository.findById(id)
                                .orElseThrow(() -> new BadRequestException(
                                                ErrorCode.ResourceNotFound,
                                                "Pollution report not found"));
        }

        private PollutionReportDto toDto(PollutionReport report) {
                PollutionReportDto dto = new PollutionReportDto(report);

                String bucketUrl = report.getImageUrl();

                if (bucketUrl != null && !bucketUrl.isBlank()) {
                        String signedUrl = storageUtil.getSignedUrlFromBucketUrl(bucketUrl);

                        dto.setImageUrl(trimOrNull(signedUrl));
                }

                return dto;
        }

        private RequestContext requireLogin() {
                RequestContext context = RequestContext.get();

                if (context == null
                                || context.getAccountId() == null
                                || context.getAccountRole() == null) {

                        throw new BadRequestException(
                                        ErrorCode.InvalidCredentials,
                                        "Login required");
                }

                return context;
        }

        private void requireAdmin() {
                RequestContext context = requireLogin();

                if (context.getAccountRole() != AccountRole.Admin) {
                        throw new BadRequestException(
                                        ErrorCode.InvalidCredentials,
                                        "Administrator access required");
                }
        }

        private String trimOrNull(String value) {
                return value == null || value.isBlank()
                                ? null
                                : value.trim();
        }

}