package org.our_place.room.infra.persistence.repository;

import org.our_place.room.domain.entity.LkpRoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LkpRoomStatusRepository extends JpaRepository<LkpRoomStatus, String> {
}