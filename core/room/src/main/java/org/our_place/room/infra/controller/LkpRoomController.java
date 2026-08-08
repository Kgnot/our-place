package org.our_place.room.infra.controller;


import lombok.RequiredArgsConstructor;
import org.our_place.room.application.service.LkpRoomService;
import org.our_place.room.domain.entity.LkpRelationshipType;
import org.our_place.room.domain.entity.LkpRoomStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/room")
@RequiredArgsConstructor
public class LkpRoomController {

    private final LkpRoomService lkpService;

    @GetMapping("/relationship-types")
    public ResponseEntity<List<LkpRelationshipType>> getRelationshipTypes() {
        return ResponseEntity.ok(lkpService.getRelationshipTypes());
    }

    @GetMapping("/room-statuses")
    public ResponseEntity<List<LkpRoomStatus>> getRoomStatuses() {
        return ResponseEntity.ok(lkpService.getRoomStatuses());
    }
}
