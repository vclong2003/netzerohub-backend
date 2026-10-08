package com.nrma.green_charging_station.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nrma.green_charging_station.entity.Plug;

public interface IPlugRepository extends JpaRepository<Plug, Integer> {
    public Optional<Plug> findByIdAndStationId(Integer plugId, Integer stationId);
}
