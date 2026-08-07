package org.our_place.map.persistence.repository;

import org.our_place.map.persistence.entity.SavedPlace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SavedPlaceRepository extends JpaRepository<SavedPlace, UUID> {

    List<SavedPlace> findByRoomIdOrderByCreatedAtDesc(UUID roomId);

    Optional<SavedPlace> findByIdAndRoomId(UUID id, UUID roomId);

    boolean existsBySourceMediaId(UUID sourceMediaId);
}