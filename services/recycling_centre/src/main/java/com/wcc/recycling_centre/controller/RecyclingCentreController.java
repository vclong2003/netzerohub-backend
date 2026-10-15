package com.wcc.recycling_centre.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netzerohub.common.annotation.RequireRole;
import com.netzerohub.common.dto.PageResponse;
import com.netzerohub.common.enums.AccountRole;
import com.wcc.recycling_centre.dto.CentreDto;
import com.wcc.recycling_centre.dto.WasteTypeDto;
import com.wcc.recycling_centre.entity.Centre;
import com.wcc.recycling_centre.entity.WasteType;
import com.wcc.recycling_centre.form.CreateCentreForm;
import com.wcc.recycling_centre.form.CreateWasteTypeForm;
import com.wcc.recycling_centre.form.GetCentresForm;
import com.wcc.recycling_centre.form.UpdateCentreForm;
import com.wcc.recycling_centre.form.UpdateWasteTypeForm;
import com.wcc.recycling_centre.service.RecyclingCentreService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/recycling-centres")
@RequiredArgsConstructor
public class RecyclingCentreController {

        private final RecyclingCentreService recyclingCentreService;

        @GetMapping("")
        public ResponseEntity<PageResponse<CentreDto>> getCentres(
                        @ModelAttribute GetCentresForm form) {

                PageResponse<Centre> pageCentre = recyclingCentreService.getCentres(form);

                PageResponse<CentreDto> pageCentreDto = new PageResponse<>();

                BeanUtils.copyProperties(
                                pageCentre,
                                pageCentreDto,
                                "data");

                pageCentreDto.setData(
                                pageCentre.getData()
                                                .stream()
                                                .map(CentreDto::new)
                                                .toList());

                return ResponseEntity.ok(pageCentreDto);
        }

        @GetMapping("/{id}")
        public ResponseEntity<CentreDto> getCentreById(
                        @PathVariable("id") Integer id) {

                Centre centre = recyclingCentreService.getCentreById(id);

                CentreDto centreDto = new CentreDto(centre);

                centreDto.setAcceptedWasteTypes(
                                centre.getAcceptedWasteTypes()
                                                .stream()
                                                .map(WasteTypeDto::new)
                                                .toList());

                return ResponseEntity.ok(centreDto);
        }

        @RequireRole({ AccountRole.Admin })
        @PostMapping("")
        public ResponseEntity<CentreDto> createCentre(
                        @Valid @RequestBody CreateCentreForm form) {

                Centre centre = recyclingCentreService.createCentre(form);

                return ResponseEntity.ok(
                                new CentreDto(centre));
        }

        @RequireRole({ AccountRole.Admin })
        @PutMapping("/{id}")
        public ResponseEntity<CentreDto> updateCentre(
                        @PathVariable("id") Integer id,
                        @RequestBody UpdateCentreForm form) {

                Centre centre = recyclingCentreService.updateCentre(id, form);

                return ResponseEntity.ok(
                                new CentreDto(centre));
        }

        @RequireRole({ AccountRole.Admin })
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteCentre(
                        @PathVariable("id") Integer id) {

                recyclingCentreService.deleteCentre(id);

                return ResponseEntity.noContent().build();
        }

        @GetMapping("/waste-types")
        public ResponseEntity<List<WasteTypeDto>> getAllWasteTypes() {
                return ResponseEntity.ok(
                                recyclingCentreService.getAllWasteTypes().stream()
                                                .map(wasteType -> new WasteTypeDto(wasteType)).toList());
        }

        @RequireRole({ AccountRole.Admin })
        @PostMapping("/waste-types")
        public ResponseEntity<WasteTypeDto> createWasteType(
                        @Valid @RequestBody CreateWasteTypeForm form) {

                WasteType wasteType = recyclingCentreService.createWasteType(form);

                return ResponseEntity.ok(
                                new WasteTypeDto(wasteType));
        }

        @RequireRole({ AccountRole.Admin })
        @PutMapping("/waste-types/{wasteTypeId}")
        public ResponseEntity<WasteTypeDto> updateWasteType(
                        @PathVariable("wasteTypeId") Integer wasteTypeId,
                        @RequestBody UpdateWasteTypeForm form) {

                WasteType wasteType = recyclingCentreService.updateWasteType(
                                wasteTypeId,
                                form);

                return ResponseEntity.ok(
                                new WasteTypeDto(wasteType));
        }

        @RequireRole({ AccountRole.Admin })
        @DeleteMapping("/waste-types/{wasteTypeId}")
        public ResponseEntity<Void> deleteWasteType(
                        @PathVariable("wasteTypeId") Integer wasteTypeId) {

                recyclingCentreService.deleteWasteType(wasteTypeId);

                return ResponseEntity.noContent().build();
        }
}