package com.nrma.green_charging_station.form;

import com.nrma.green_charging_station.enums.PlugType;
import com.nrma.green_charging_station.enums.StationStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetChargingStationsForm {
    private String name;
    private Double latitude;
    private Double longitude;
    private StationStatus status;
    private Integer renewableEnergyPercent;

    private PlugType plugType;

    private Integer page;
    private Integer pageSize;
}
