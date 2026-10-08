package com.nrma.green_charging_station.form;

import java.math.BigDecimal;

import com.nrma.green_charging_station.enums.CurrentType;
import com.nrma.green_charging_station.enums.PlugStatus;
import com.nrma.green_charging_station.enums.PlugType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreatePlugForm {
    @NotBlank(message = "Identifier is required")
    private String identifier;

    @NotNull(message = "Status is required")
    private PlugStatus status;

    @NotNull(message = "Type is required")
    private PlugType type;

    @NotNull(message = "Current type is required")
    private CurrentType currentType;

    @NotNull(message = "Power (kW) is required")
    private BigDecimal powerKw;
}
