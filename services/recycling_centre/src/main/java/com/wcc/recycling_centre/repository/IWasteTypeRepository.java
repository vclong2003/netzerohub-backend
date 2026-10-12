package com.wcc.recycling_centre.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.wcc.recycling_centre.entity.WasteType;

public interface IWasteTypeRepository extends JpaRepository<WasteType, Integer> {
    @Modifying
    @Query(value = "DELETE FROM centre_waste_type WHERE waste_type_id = :wasteTypeId", nativeQuery = true)
    void deleteCentreAssociations(@Param("wasteTypeId") Integer wasteTypeId);
}