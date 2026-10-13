package com.netzerohub.pollution_report.form;

import com.netzerohub.pollution_report.enums.PollutionType;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreatePollutionReportForm {

    @NotBlank(message = "Title is required")
    @Size(max = 200)
    private String title;

    @NotBlank(message = "Description is required")
    @Size(max = 10000)
    private String description;

    @NotNull(message = "Pollution type is required")
    private PollutionType type;

    @Size(max = 2048)
    private String imageUrl;

    @Size(max = 500)
    private String address;

    @Valid
    private Coordinates coordinates;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Coordinates {

        @NotNull(message = "Latitude is required")
        @DecimalMin("-90")
        @DecimalMax("90")
        private Double latitude;

        @NotNull(message = "Longitude is required")
        @DecimalMin("-180")
        @DecimalMax("180")
        private Double longitude;
    }
}