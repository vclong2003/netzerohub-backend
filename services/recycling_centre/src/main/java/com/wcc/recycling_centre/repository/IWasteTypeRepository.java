package com.wcc.recycling_centre.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wcc.recycling_centre.entity.WasteType;

public interface IWasteTypeRepository extends JpaRepository<WasteType, Integer> {

}