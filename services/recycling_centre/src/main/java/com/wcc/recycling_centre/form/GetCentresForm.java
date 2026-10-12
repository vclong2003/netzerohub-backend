package com.wcc.recycling_centre.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetCentresForm {
    private String name;
    private Double latitude;
    private Double longitude;

    private Integer page;
    private Integer pageSize;
}