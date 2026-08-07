package org.our_place.map.service;


import lombok.RequiredArgsConstructor;
import org.our_place.map.service.dto.PlaceCategoryDto;
import org.our_place.map.service.mapper.MapMapper;
import org.our_place.map.persistence.repository.LkpPlaceCategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceCategoryQueryService {

    private final LkpPlaceCategoryRepository categoryRepository;

    public List<PlaceCategoryDto> findAll() {
        return categoryRepository.findAll().stream()
                .map(MapMapper::toDto)
                .toList();
    }
}