package com.wcc.recycling_centre.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Centre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = true)
    private List<String> imageUrls = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "centre_waste_type",
        joinColumns = @JoinColumn(name = "centre_id"),
        inverseJoinColumns = @JoinColumn(name = "waste_type_id")
    )
    private List<WasteType> acceptedWasteTypes = new ArrayList<>();
}