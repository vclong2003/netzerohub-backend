package com.nrma.green_charging_station.form;

import java.math.BigDecimal;

import com.nrma.green_charging_station.enums.CurrentType;
import com.nrma.green_charging_station.enums.PlugStatus;
import com.nrma.green_charging_station.enums.PlugType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePlugForm {
    private String identifier;
    private PlugStatus status;
    private PlugType type;
    private CurrentType currentType;
    private BigDecimal powerKw;
}
