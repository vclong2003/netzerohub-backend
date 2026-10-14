package com.netzerohub.pollution_report.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PollutionReportUpdateEvent {
    String email;

    String userName;
    String reportTitle;
    String reportDateTime;
}
