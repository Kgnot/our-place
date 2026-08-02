package org.our_place.common.shared.controller;


import org.our_place.common.shared.dto.SharedItemDto;
import org.our_place.common.shared.service.ArchitectureScannerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/architecture")
public class ArchitectureController {

    private final ArchitectureScannerService scannerService;

    public ArchitectureController(ArchitectureScannerService scannerService) {
        this.scannerService = scannerService;
    }

    @GetMapping("/shared")
    public Map<String, List<SharedItemDto>> getSharedItems() {
        List<SharedItemDto> items = scannerService.scanSharedItems();

        return Map.of(
                "apis", items.stream().filter(i -> i.type().equals("API")).toList(),
                "domains", items.stream().filter(i -> i.type().equals("DOMAIN")).toList()
        );
    }
}