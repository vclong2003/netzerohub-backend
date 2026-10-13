package com.netzerohub.pollution_report.dto;

import java.time.Instant;

import com.netzerohub.pollution_report.entity.PollutionReport;
import com.netzerohub.pollution_report.enums.PollutionType;
import com.netzerohub.pollution_report.enums.PollutionReportStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PollutionReportDto {

    private Integer id;
    private String title;
    private String description;
    private PollutionType type;
    private PollutionReportStatus status;
    private String imageUrl;
    private String address;
    private Double latitude;
    private Double longitude;
    private Instant createdAt;
    private Instant updatedAt;

    public PollutionReportDto(PollutionReport entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.description = entity.getDescription();
        this.type = entity.getType();
        this.status = entity.getStatus();
        this.imageUrl = entity.getImageUrl();
        this.address = entity.getAddress();
        this.latitude = entity.getLatitude();
        this.longitude = entity.getLongitude();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }
}