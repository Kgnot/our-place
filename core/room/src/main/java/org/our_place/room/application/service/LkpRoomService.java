package org.our_place.room.application.service;

import java.util.List;

import org.our_place.room.domain.entity.LkpRelationshipType;
import org.our_place.room.domain.entity.LkpRoomStatus;
import org.our_place.room.infra.persistence.repository.LkpRelationshipTypeRepository;
import org.our_place.room.infra.persistence.repository.LkpRoomStatusRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * Servicio de solo lectura para catalogos (lookups).
 * Los datos son estaticos/poco cambiantes, por eso se cachean.
 */
@Service
@RequiredArgsConstructor
public class LkpRoomService {

    private final LkpRelationshipTypeRepository relationshipTypeRepository;
    private final LkpRoomStatusRepository roomStatusRepository;

    @Cacheable("relationshipTypes")
    public List<LkpRelationshipType> getRelationshipTypes() {
        return relationshipTypeRepository.findAll();
    }

    @Cacheable("roomStatuses")
    public List<LkpRoomStatus> getRoomStatuses() {
        return roomStatusRepository.findAll();
    }
}