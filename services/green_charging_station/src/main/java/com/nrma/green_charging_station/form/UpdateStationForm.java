package com.nrma.green_charging_station.form;

import java.util.List;

import com.nrma.green_charging_station.enums.StationStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStationForm {
    private String name;

    private Double latitude;

    private Double longitude;

    private Integer renewableEnergyPercent;

    private StationStatus status;

    private List<String> additionalImageUrls;

    private List<String> removalImageUrls;
}
