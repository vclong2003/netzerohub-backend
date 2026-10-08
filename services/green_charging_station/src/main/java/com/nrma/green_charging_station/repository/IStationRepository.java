package com.nrma.green_charging_station.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.nrma.green_charging_station.entity.Station;

public interface IStationRepository extends JpaRepository<Station, Integer>, JpaSpecificationExecutor<Station> {

}
