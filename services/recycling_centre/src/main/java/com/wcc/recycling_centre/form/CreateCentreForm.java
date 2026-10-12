package com.wcc.recycling_centre.form;

import java.time.LocalTime;
import java.util.List;

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
public class CreateCentreForm {

    @NotBlank(message = "Centre name is required")
    private String name;

    @NotNull(message = "Latitude is required")
    private Double latitude;

    @NotNull(message = "Longitude is required")
    private Double longitude;

    private LocalTime openingTime;

    private LocalTime closingTime;

    private String address;

    private List<String> imageUrls;

    private List<String> infoSheetUrls;
}