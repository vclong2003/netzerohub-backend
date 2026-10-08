package com.nrma.green_charging_station.service;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.netzerohub.common.exception.BadRequestException;
import com.netzerohub.common.util.ObjectUtils;
import com.netzerohub.common.dto.PageResponse;
import com.netzerohub.common.enums.ErrorCode;
import com.nrma.green_charging_station.entity.Plug;
import com.nrma.green_charging_station.entity.Station;
import com.nrma.green_charging_station.form.CreatePlugForm;
import com.nrma.green_charging_station.form.CreateStationForm;
import com.nrma.green_charging_station.form.GetChargingStationsForm;
import com.nrma.green_charging_station.form.UpdatePlugForm;
import com.nrma.green_charging_station.form.UpdateStationForm;
import com.nrma.green_charging_station.repository.IPlugRepository;
import com.nrma.green_charging_station.repository.IStationRepository;
import com.nrma.green_charging_station.repository.specification.StationSpecification;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChargingStationService {
        private final IStationRepository stationRepository;
        private final IPlugRepository plugRepository;

        @Transactional
        public PageResponse<Station> getChargingStations(GetChargingStationsForm form) {
                Pageable pageable = PageRequest.of(
                                form.getPage(),
                                form.getPageSize(),
                                Sort.by(Sort.Direction.DESC, "id"));

                Specification<Station> spec = StationSpecification.filterByForm(form);

                Page<Station> pageResult = stationRepository.findAll(spec, pageable);

                return PageResponse.<Station>builder()
                                .data(pageResult.getContent())
                                .page(form.getPage())
                                .pageSize(pageResult.getSize())
                                .totalElements(pageResult.getTotalElements())
                                .totalPages(pageResult.getTotalPages())
                                .hasNext(pageResult.hasNext())
                                .hasPrevious(pageResult.hasPrevious())
                                .build();

        }

        public Station getChargingStationById(Integer id) {
                return stationRepository.findById(id)
                                .orElseThrow(() -> new BadRequestException(ErrorCode.ResourceNotFound,
                                                "Charging station not found with id "));
        }

        @Transactional
        public Station createChargingStation(CreateStationForm form) {
                Station station = new Station();
                station.setName(form.getName());
                station.setLatitude(form.getLatitude());
                station.setLongitude(form.getLongitude());
                station.setRenewableEnergyPercent(form.getRenewableEnergyPercent());
                station.setImageUrls(form.getImageUrls());
                return stationRepository.save(station);
        }

        @Transactional
        public Station updateChargingStation(Integer id, UpdateStationForm form) {
                Station station = getChargingStationById(id);
                BeanUtils.copyProperties(form, station, ObjectUtils.getNullPropertyNames(form));
                return stationRepository.save(station);
        }

        public void deleteChargingStation(Integer id) {
                Station station = getChargingStationById(id);
                stationRepository.delete(station);
        }

        @Transactional
        public Plug createPlug(Integer stationId, CreatePlugForm form) {
                Station station = getChargingStationById(stationId);

                Plug plug = new Plug();
                plug.setIdentifier(form.getIdentifier());
                plug.setStatus(form.getStatus());
                plug.setType(form.getType());
                plug.setCurrentType(form.getCurrentType());
                plug.setPowerKw(form.getPowerKw());
                plug.setStation(station);

                return plugRepository.save(plug);
        }

        @Transactional
        public Plug updatePlug(Integer stationId, Integer plugId, UpdatePlugForm form) {

                Plug plugToUpdate = plugRepository.findById(plugId)
                                .orElseThrow(() -> new BadRequestException(ErrorCode.ResourceNotFound,
                                                "Plug not found"));

                BeanUtils.copyProperties(form, plugToUpdate, ObjectUtils.getNullPropertyNames(form));

                return plugRepository.save(plugToUpdate);

        }

        @Transactional
        public void deletePlug(Integer stationId, Integer plugId) {
                Plug plugToDelete = plugRepository.findById(plugId)
                                .orElseThrow(() -> new BadRequestException(ErrorCode.ResourceNotFound,
                                                "Plug not found"));

                if (!plugToDelete.getStation().getId().equals(stationId)) {
                        throw new BadRequestException(ErrorCode.InvalidInput,
                                        "Plug does not belong to the specified station");
                }

                plugRepository.delete(plugToDelete);
        }
}
