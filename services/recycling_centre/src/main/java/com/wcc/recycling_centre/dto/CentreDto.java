package com.wcc.recycling_centre.dto;

import java.util.ArrayList;
import java.util.List;

import com.wcc.recycling_centre.entity.Centre;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CentreDto {

    private Integer id;
    private String name;
    private Double latitude;
    private Double longitude;

    private List<String> imageUrls;

    private List<WasteTypeDto> acceptedWasteTypes = new ArrayList<>();

    public CentreDto(Centre entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.latitude = entity.getLatitude();
        this.longitude = entity.getLongitude();
        this.imageUrls = entity.getImageUrls();
    }
}