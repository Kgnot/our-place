package org.our_place.map.controller;

import lombok.RequiredArgsConstructor;
import org.our_place.map.controller.response.PlaceCategoryResponse;
import org.our_place.map.service.PlaceCategoryQueryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/map/place-categories")
@RequiredArgsConstructor
public class PlaceCategoryController {

    private final PlaceCategoryQueryService placeCategoryQueryService;

    @GetMapping
    public List<PlaceCategoryResponse> list() {
        return placeCategoryQueryService.findAll().stream()
                .map(PlaceCategoryResponse::from)
                .toList();
    }
}