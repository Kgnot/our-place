package org.our_place.map.controller.response;

import org.our_place.map.service.dto.PlaceCategoryDto;

public record PlaceCategoryResponse(
        String code,
        String name,
        String iconUrl
) {
    public static PlaceCategoryResponse from(PlaceCategoryDto dto) {
        return new PlaceCategoryResponse(dto.code(), dto.name(), dto.iconUrl());
    }
}