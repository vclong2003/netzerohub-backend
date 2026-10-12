package com.wcc.recycling_centre.form;

import java.time.LocalTime;
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

    private LocalTime openingTime;

    private LocalTime closingTime;

    private List<Integer> acceptedWasteTypeIds;

    private List<String> additionalInfoSheetUrls;
    private List<String> removedInfoSheetUrls;

    private List<String> additionalImageUrls;
    private List<String> removedImageUrls;

    private String address;

}