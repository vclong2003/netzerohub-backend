package com.netzerohub.pollution_report.form;

import java.util.List;

import com.netzerohub.pollution_report.enums.PollutionReportStatus;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdatePollutionReportStatusForm {

    @NotEmpty(message = "Select at least one report")
    @Size(max = 100)
    private List<@NotNull @Positive Integer> ids;

    @NotNull(message = "Status is required")
    private PollutionReportStatus status;
}