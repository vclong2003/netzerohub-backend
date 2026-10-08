package com.nrma.green_charging_station.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netzerohub.common.dto.PageResponse;
import com.nrma.green_charging_station.dto.PlugDto;
import com.nrma.green_charging_station.dto.StationDto;
import com.nrma.green_charging_station.entity.Plug;
import com.nrma.green_charging_station.entity.Station;
import com.nrma.green_charging_station.form.CreatePlugForm;
import com.nrma.green_charging_station.form.CreateStationForm;
import com.nrma.green_charging_station.form.GetChargingStationsForm;
import com.nrma.green_charging_station.form.UpdatePlugForm;
import com.nrma.green_charging_station.form.UpdateStationForm;
import com.nrma.green_charging_station.service.ChargingStationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("api/green-charging-stations")
@RequiredArgsConstructor
public class ChargingStationController {
    private final ChargingStationService chargingStationService;

    @GetMapping("")
    public ResponseEntity<PageResponse<StationDto>> getChargingStations(@ModelAttribute GetChargingStationsForm form) {
        PageResponse<Station> pageStation = chargingStationService.getChargingStations(form);

        PageResponse<StationDto> pageStationDto = new PageResponse<>();

        BeanUtils.copyProperties(pageStation, pageStationDto, "data");
        pageStationDto.setData(pageStation.getData().stream().map(station -> new StationDto(station)).toList());

        return ResponseEntity.ok(pageStationDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StationDto> getChargingStationById(@PathVariable("id") Integer id) {
        Station station = chargingStationService.getChargingStationById(id.intValue());
        StationDto stationDto = new StationDto(station);
        stationDto.setImageUrls(station.getImageUrls());
        stationDto.setPlugs(station.getPlugs().stream().map(plug -> new PlugDto(plug)).toList());
        return ResponseEntity.ok(stationDto);
    }

    @PostMapping("")
    public ResponseEntity<StationDto> createChargingStation(@Valid @RequestBody CreateStationForm form) {
        Station station = chargingStationService.createChargingStation(form);
        StationDto stationDto = new StationDto(station);
        return ResponseEntity.ok(stationDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StationDto> updateChargingStation(@PathVariable("id") Integer id,
            @RequestBody UpdateStationForm form) {
        Station station = chargingStationService.updateChargingStation(id.intValue(), form);
        StationDto stationDto = new StationDto(station);
        return ResponseEntity.ok(stationDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChargingStation(@PathVariable("id") Integer id) {
        chargingStationService.deleteChargingStation(id.intValue());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{stationId}/plugs")
    public ResponseEntity<PlugDto> createPlug(@PathVariable("stationId") Integer stationId,
            @Valid @RequestBody CreatePlugForm form) {
        Plug plug = chargingStationService.createPlug(stationId.intValue(), form);
        return ResponseEntity.ok(new PlugDto(plug));
    }

    @PutMapping("/{stationId}/plugs/{plugId}")
    public ResponseEntity<PlugDto> updatePlug(@PathVariable("stationId") Integer stationId,
            @PathVariable("plugId") Integer plugId,
            @RequestBody UpdatePlugForm form) {
        Plug plug = chargingStationService.updatePlug(stationId.intValue(), plugId.intValue(), form);
        return ResponseEntity.ok(new PlugDto(plug));
    }

    @DeleteMapping("/{stationId}/plugs/{plugId}")
    public ResponseEntity<Void> deletePlug(@PathVariable("stationId") Integer stationId,
            @PathVariable("plugId") Integer plugId) {
        chargingStationService.deletePlug(stationId.intValue(), plugId.intValue());
        return ResponseEntity.ok().build();
    }
}
