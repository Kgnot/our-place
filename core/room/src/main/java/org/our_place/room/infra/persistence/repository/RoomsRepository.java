package org.our_place.room.infra.persistence.repository;

import org.our_place.room.domain.entity.Rooms;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoomsRepository extends JpaRepository<Rooms, UUID> {
}