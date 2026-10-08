package com.nrma.green_charging_station.dto;

import java.util.ArrayList;
import java.util.List;

import com.nrma.green_charging_station.entity.Station;
import com.nrma.green_charging_station.enums.StationStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StationDto {
    private Integer id;
    private String name;
    private Double latitude;
    private Double longitude;
    private Integer renewableEnergyPercent;
    private StationStatus status = StationStatus.Active;

    private List<String> imageUrls;

    private List<PlugDto> plugs = new ArrayList<>();

    public StationDto(Station entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.latitude = entity.getLatitude();
        this.longitude = entity.getLongitude();
        this.renewableEnergyPercent = entity.getRenewableEnergyPercent();
        this.status = entity.getStatus();
    }
}
