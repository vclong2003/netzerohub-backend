package com.wcc.recycling_centre.dto;

import com.wcc.recycling_centre.entity.WasteType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WasteTypeDto {

    private Integer id;
    private String name;
    private String code;

    public WasteTypeDto(WasteType entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.code = entity.getCode();
    }
}