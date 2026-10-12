package com.wcc.recycling_centre.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.wcc.recycling_centre.entity.Centre;

public interface ICentreRepository
        extends JpaRepository<Centre, Integer>,
                JpaSpecificationExecutor<Centre> {

}