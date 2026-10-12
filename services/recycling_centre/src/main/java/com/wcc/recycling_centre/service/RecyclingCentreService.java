package com.wcc.recycling_centre.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.netzerohub.common.dto.PageResponse;
import com.netzerohub.common.enums.ErrorCode;
import com.netzerohub.common.exception.BadRequestException;
import com.netzerohub.common.util.ObjectUtils;
import com.netzerohub.common.util.StorageUtil;
import com.wcc.recycling_centre.entity.Centre;
import com.wcc.recycling_centre.entity.WasteType;
import com.wcc.recycling_centre.form.CreateCentreForm;
import com.wcc.recycling_centre.form.CreateWasteTypeForm;
import com.wcc.recycling_centre.form.GetCentresForm;
import com.wcc.recycling_centre.form.UpdateCentreForm;
import com.wcc.recycling_centre.form.UpdateWasteTypeForm;
import com.wcc.recycling_centre.repository.ICentreRepository;
import com.wcc.recycling_centre.repository.IWasteTypeRepository;
import com.wcc.recycling_centre.repository.specification.CentreSpecification;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecyclingCentreService {

    private final ICentreRepository centreRepository;
    private final IWasteTypeRepository wasteTypeRepository;
    private final StorageUtil storageUtil;

    @Transactional
    public PageResponse<Centre> getCentres(GetCentresForm form) {

        int page = form.getPage() != null ? form.getPage() : 0;
        int pageSize = form.getPageSize() != null ? form.getPageSize() : 10;

        Pageable pageable = PageRequest.of(
                page,
                pageSize,
                Sort.by(Sort.Direction.DESC, "id"));

        Specification<Centre> spec = CentreSpecification.filterByForm(form);

        Page<Centre> pageResult = centreRepository.findAll(spec, pageable);

        return PageResponse.<Centre>builder()
                .data(pageResult.getContent())
                .page(page)
                .pageSize(pageResult.getSize())
                .totalElements(pageResult.getTotalElements())
                .totalPages(pageResult.getTotalPages())
                .hasNext(pageResult.hasNext())
                .hasPrevious(pageResult.hasPrevious())
                .build();
    }

    public Centre getCentreById(Integer id) {
        Centre centre = centreRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(
                        ErrorCode.ResourceNotFound,
                        "Recycling centre not found with id " + id));

        centre.setImageUrls(centre.getImageUrls().stream()
                .map(bucketUrl -> storageUtil.getSignedUrlFromBucketUrl(bucketUrl)).toList());

        centre.setInfoSheetUrls(centre.getInfoSheetUrls().stream()
                .map(bucketUrl -> storageUtil.getSignedUrlFromBucketUrl(bucketUrl)).toList());

        return centre;
    }

    @Transactional
    public Centre createCentre(CreateCentreForm form) {

        Centre centre = new Centre();

        centre.setName(form.getName());
        centre.setLatitude(form.getLatitude());
        centre.setLongitude(form.getLongitude());
        centre.setAddress(form.getAddress());
        centre.setOpeningTime(form.getOpeningTime());
        centre.setClosingTime(form.getClosingTime());

        if (form.getImageUrls() != null) {
            centre.setImageUrls(form.getImageUrls());
        }

        if (form.getInfoSheetUrls() != null) {
            centre.setInfoSheetUrls(form.getInfoSheetUrls());
        }

        return centreRepository.save(centre);
    }

    @Transactional
    public Centre updateCentre(Integer id, UpdateCentreForm form) {

        Centre centre = getCentreById(id);

        BeanUtils.copyProperties(
                form,
                centre,
                ObjectUtils.getNullPropertyNames(form));

        // waste types
        if (form.getAcceptedWasteTypeIds() != null) {
            List<WasteType> wasteTypes = wasteTypeRepository.findAllById(form.getAcceptedWasteTypeIds());
            if (wasteTypes.size() != form.getAcceptedWasteTypeIds().size()) {
                throw new IllegalArgumentException("One or more WasteType IDs are invalid");
            }
            centre.getAcceptedWasteTypes().clear();
            centre.getAcceptedWasteTypes().addAll(wasteTypes);
        }

        // images
        List<String> additionalImageUrls = form.getAdditionalImageUrls();
        if (additionalImageUrls != null && !additionalImageUrls.isEmpty()) {
            centre.getImageUrls().addAll(additionalImageUrls);
        }
        List<String> removedImageUrls = form.getRemovedImageUrls();
        if (removedImageUrls != null && !removedImageUrls.isEmpty()) {
            List<String> removedBucketUrls = removedImageUrls.stream()
                    .map(bucketUrl -> storageUtil.getBucketUrlFromSignedUrl(bucketUrl))
                    .toList();
            centre.getImageUrls().removeAll(removedBucketUrls);
        }

        // info sheets
        List<String> additionalInfoSheetUrls = form.getAdditionalInfoSheetUrls();
        if (additionalInfoSheetUrls != null && !additionalInfoSheetUrls.isEmpty()) {
            centre.getInfoSheetUrls().addAll(additionalInfoSheetUrls);
        }
        List<String> removedInfoSheetUrls = form.getRemovedInfoSheetUrls();
        if (removedInfoSheetUrls != null && !removedInfoSheetUrls.isEmpty()) {
            List<String> removedBucketUrls = removedInfoSheetUrls.stream()
                    .map(bucketUrl -> storageUtil.getBucketUrlFromSignedUrl(bucketUrl))
                    .toList();
            centre.getInfoSheetUrls().removeAll(removedBucketUrls);
        }

        return centreRepository.save(centre);
    }

    @Transactional
    public List<WasteType> getAllWasteTypes() {
        return wasteTypeRepository.findAll();
    }

    @Transactional
    public void deleteCentre(Integer id) {

        Centre centre = centreRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(
                        ErrorCode.ResourceNotFound,
                        "Recycling centre not found with id " + id));

        centreRepository.delete(centre);
    }

    @Transactional
    public WasteType createWasteType(CreateWasteTypeForm form) {

        WasteType wasteType = new WasteType();

        wasteType.setName(form.getName());

        return wasteTypeRepository.save(wasteType);
    }

    @Transactional
    public WasteType updateWasteType(
            Integer wasteTypeId,
            UpdateWasteTypeForm form) {

        WasteType wasteType = wasteTypeRepository.findById(wasteTypeId)
                .orElseThrow(() -> new BadRequestException(
                        ErrorCode.ResourceNotFound,
                        "Waste type not found with id " + wasteTypeId));

        BeanUtils.copyProperties(
                form,
                wasteType,
                ObjectUtils.getNullPropertyNames(form));

        return wasteTypeRepository.save(wasteType);
    }

    @Transactional
    public void deleteWasteType(Integer wasteTypeId) {
        WasteType wasteType = wasteTypeRepository.findById(wasteTypeId)
                .orElseThrow(() -> new BadRequestException(
                        ErrorCode.ResourceNotFound,
                        "Waste type not found with id " + wasteTypeId));

        wasteTypeRepository.deleteCentreAssociations(wasteTypeId);

        wasteTypeRepository.deleteById(wasteType.getId());
    }
}