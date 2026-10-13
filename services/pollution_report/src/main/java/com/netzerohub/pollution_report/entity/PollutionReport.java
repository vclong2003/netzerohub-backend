package com.netzerohub.pollution_report.entity;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.netzerohub.pollution_report.enums.PollutionType;
import com.netzerohub.pollution_report.enums.PollutionReportStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pollution_report")
@Getter
@Setter
@NoArgsConstructor
public class PollutionReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer accountId;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PollutionType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PollutionReportStatus status = PollutionReportStatus.PENDING;

    @Column(length = 2048)
    private String imageUrl;

    @Column(length = 500)
    private String address;

    private Double latitude;

    private Double longitude;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;
}