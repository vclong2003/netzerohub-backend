package com.netzerohub.pollution_report.form;

import com.netzerohub.pollution_report.enums.PollutionType;
import com.netzerohub.pollution_report.enums.PollutionReportStatus;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetPollutionReportsForm {

    @Size(max = 200)
    private String search;

    private PollutionType type;

    private PollutionReportStatus status;

    @NotNull
    @Min(0)
    private Integer page = 0;

    @NotNull
    @Min(1)
    @Max(100)
    private Integer pageSize = 10;
}