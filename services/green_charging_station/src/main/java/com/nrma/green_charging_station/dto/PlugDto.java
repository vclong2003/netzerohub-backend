package com.nrma.green_charging_station.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.nrma.green_charging_station.entity.Plug;
import com.nrma.green_charging_station.enums.CurrentType;
import com.nrma.green_charging_station.enums.PlugStatus;
import com.nrma.green_charging_station.enums.PlugType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlugDto {
    private Integer id;
    private String identifier;
    private PlugStatus status = PlugStatus.Available;
    private PlugType type;
    private CurrentType currentType;
    private BigDecimal powerKw;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PlugDto(Plug entity) {
        this.id = entity.getId();
        this.identifier = entity.getIdentifier();
        this.status = entity.getStatus();
        this.type = entity.getType();
        this.currentType = entity.getCurrentType();
        this.powerKw = entity.getPowerKw();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }
}
