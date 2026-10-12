package com.wcc.recycling_centre.form;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCentreForm {

    private String name;

    private Double latitude;

    private Double longitude;

    private List<String> imageUrls;
    private String address;
    private String openingHours;
    private List<String> infoSheetUrls;
}