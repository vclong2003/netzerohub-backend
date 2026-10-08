package com.nrma.green_charging_station.form;

import java.util.List;

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

    private List<String> imageUrls;
}
